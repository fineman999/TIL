package router

import (
	"context"
	"fmt"
	"fx-server/app/controller"
	"github.com/gin-gonic/gin"
	"go.uber.org/fx"
	"net/http"
	"time"
)

func NewNetwork(lc fx.Lifecycle, router *gin.Engine) *http.Server {
	srv := &http.Server{
		Addr:         ":" + "8080",
		Handler:      router,
		ReadTimeout:  10 * time.Minute,
		WriteTimeout: 10 * time.Minute,
	}
	lc.Append(fx.Hook{
		OnStart: func(ctx context.Context) error {
			fmt.Printf("Starting server on port: %s\n", srv.Addr) // 비동기 블록 밖에서 로그 출력
			go func() {
				if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
					fmt.Printf("Server error: %v\n", err)
				}
			}()
			return nil
		},
		OnStop: func(ctx context.Context) error {
			return endServer(srv)
		},
	})
	return srv
}

func endServer(srv *http.Server) error {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	fmt.Printf("Starting graceful shutdown with 5-second timeout at %s\n", time.Now().Format(time.RFC3339))
	err := srv.Shutdown(ctx)
	if err != nil {
		fmt.Printf("Graceful shutdown failed: %v at %s\n", err, time.Now().Format(time.RFC3339))
	} else {
		fmt.Printf("Graceful shutdown completed at %s\n", time.Now().Format(time.RFC3339))
	}
	return srv.Shutdown(ctx)
}

func SetupRouter(r *gin.Engine, userController *controller.UserController, authMiddleware gin.HandlerFunc) {
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
			fmt.Println("Received test-delay request at", time.Now().Format(time.RFC3339))
			time.Sleep(10 * time.Second) // 10초 대기
			fmt.Println("Test-delay request completed at", time.Now().Format(time.RFC3339))
			c.JSON(http.StatusOK, gin.H{"message": "Delayed response"})
		})
	}
}
