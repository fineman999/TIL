package router

import (
	"context"
	"fx-server/app/controller"
	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
	"go.uber.org/fx"
	"net/http"
	"time"
)

func NewLogHandler(lc fx.Lifecycle) *logrus.Logger {
	logger := logrus.New()
	logger.SetFormatter(&logrus.TextFormatter{
		FullTimestamp: true,
	})
	logger.SetLevel(logrus.DebugLevel)
	lc.Append(fx.Hook{
		OnStop: func(ctx context.Context) error {
			logger.Infof("Closing logger at %s", time.Now().Format(time.RFC3339))
			return nil
		},
	})
	return logger
}

func NewGinEngine(logger *logrus.Logger) *gin.Engine {
	engine := gin.New()
	engine.Use(gin.LoggerWithWriter(logger.Writer(), "/actuator/health"))
	engine.Use(gin.RecoveryWithWriter(logger.Writer()))
	return engine
}

func NewNetwork(lc fx.Lifecycle, router *gin.Engine, logger *logrus.Logger) *http.Server {
	srv := &http.Server{
		Addr:         ":" + "8080",
		Handler:      router,
		ReadTimeout:  10 * time.Minute,
		WriteTimeout: 10 * time.Minute,
	}
	lc.Append(fx.Hook{
		OnStart: func(ctx context.Context) error {
			logger.Printf("Starting server on port: %s\n", srv.Addr) // 비동기 블록 밖에서 로그 출력
			go func() {
				if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
					logger.Printf("Server error: %v\n", err)
				}
			}()
			return nil
		},
		OnStop: func(ctx context.Context) error {
			return endServer(srv, logger)
		},
	})
	return srv
}

func endServer(srv *http.Server, logger *logrus.Logger) error {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	logger.Printf("Starting graceful shutdown with 5-second timeout at %s\n", time.Now().Format(time.RFC3339))
	err := srv.Shutdown(ctx)
	if err != nil {
		logger.Printf("Graceful shutdown failed: %v at %s\n", err, time.Now().Format(time.RFC3339))
	} else {
		logger.Printf("Graceful shutdown completed at %s\n", time.Now().Format(time.RFC3339))
	}
	return srv.Shutdown(ctx)
}

// SetUpRouterParams 구조체 정의
type SetUpRouterParams struct {
	R              *gin.Engine
	UserController *controller.UserController
	AuthMiddleware gin.HandlerFunc
	Logger         *logrus.Logger
}

// NewSetupRouterParams는 SetUpRouterParams를 생성하는 생성자 함수
func NewSetupRouterParams(
	r *gin.Engine,
	userController *controller.UserController,
	authMiddleware gin.HandlerFunc,
	logger *logrus.Logger,
) SetUpRouterParams {
	return SetUpRouterParams{
		R:              r,
		UserController: userController,
		AuthMiddleware: authMiddleware,
		Logger:         logger,
	}
}

func SetupRouter(params SetUpRouterParams) {
	r := params.R
	userController := params.UserController
	authMiddleware := params.AuthMiddleware
	logger := params.Logger
	v1 := r.Group("/api/v1")
	{
		users := v1.Group("/users")
		users.Use(authMiddleware)
		{
			users.GET("", userController.GetAllUsers)
			users.GET("/:id", userController.GetUser)
			users.POST("", userController.CreateUser)
			users.PUT("/:id", userController.UpdateUser)
			users.DELETE("/:id", userController.DeleteUser)
		}
		// 테스트용 엔드포인트: 10초 지연
		v1.GET("/test-delay", func(c *gin.Context) {
			logger.Println("Received test-delay request at", time.Now().Format(time.RFC3339))
			time.Sleep(10 * time.Second) // 10초 대기
			logger.Println("Test-delay request completed at", time.Now().Format(time.RFC3339))
			c.JSON(http.StatusOK, gin.H{"message": "Delayed response"})
		})
	}
}
