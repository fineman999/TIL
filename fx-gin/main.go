package main

import (
	"context"
	"fx-server/app/controller"
	"fx-server/app/middleware"
	"fx-server/app/repository"
	"fx-server/app/router"
	"fx-server/app/service"
	"github.com/gin-gonic/gin"
	"go.uber.org/fx"
	"log"
	"net/http"
	"os"
)

func main() {
	app := fx.New(
		fx.Provide(
			// Logger 생성 함수 추가
			func() *log.Logger {
				return log.New(os.Stdout, "[fx-server] ", log.LstdFlags)
			},
			// Gin 엔진
			func(logger *log.Logger) *gin.Engine {
				engine := gin.New()
				engine.Use(gin.LoggerWithWriter(logger.Writer(), "/actuator/health"))
				engine.Use(gin.RecoveryWithWriter(logger.Writer()))
				return engine
			},
			router.NewNetwork,
			repository.NewUserRepository, // 인메모리 UserRepository 제공
			service.NewUserService,       // UserService 제공
			controller.NewUserController, // UserController 제공
			middleware.NewAuthMiddleware, // AuthMiddleware 제공
		),
		fx.Invoke(
			router.SetupRouter, // 라우터 설정
		),
		fx.Invoke(
			func(*http.Server) {}, // HTTP 서버 등록
		),
	)
	if err := app.Start(context.Background()); err != nil {
		log.Fatalf("Failed to start application: %v\n", err)
	}

	<-app.Done() // 애플리케이션 종료 시까지 대기

	if err := app.Stop(context.Background()); err != nil {
		log.Printf("Application stopped with expected timeout: %v\n", err)
	} else {
		log.Println("Application stopped successfully")
	}

}
