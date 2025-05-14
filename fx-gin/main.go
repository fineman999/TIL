package main

import (
	"context"
	"fx-server/app/controller"
	"fx-server/app/middleware"
	"fx-server/app/repository"
	"fx-server/app/router"
	"fx-server/app/service"
	"fx-server/config"
	"github.com/sirupsen/logrus"
	"go.uber.org/fx"
	"go.uber.org/fx/fxevent"
	"log"
	"net/http"
)

// 로깅 모듈
var loggingModule = fx.Module(
	"logging",
	fx.Provide(
		router.NewLogHandler, // Logger 생성 함수
	),
	fx.WithLogger(
		func(logger *logrus.Logger) fxevent.Logger {
			return config.NewFxLogger(logger)
		},
	),
)

// 리포지토리 모듈
var repositoryModule = fx.Module(
	"repository",
	fx.Provide(
		fx.Annotate(
			repository.NewUserRepository,
			fx.As(new(repository.UserRepository)),
		),
	),
)

// 서비스 모듈
var serviceModule = fx.Module(
	"service",
	fx.Provide(
		service.NewUserService,
	),
)

// 컨트롤러 모듈
var controllerModule = fx.Module(
	"controller",
	fx.Provide(
		controller.NewUserController,
	),
)

// 라우터 모듈
var routerModule = fx.Module(
	"router",
	fx.Provide(
		router.NewGinEngine,
		router.NewNetwork,
		middleware.NewAuthMiddleware,
		router.NewSetupRouterParams,
	),
	fx.Invoke(
		router.SetupRouter,
	),
)

// 서버 모듈
var serverModule = fx.Module(
	"server",
	fx.Invoke(
		func(srv *http.Server) {
			log.Printf("HTTP Server is ready at %s", srv.Addr)
		},
	),
)

func main() {
	app := fx.New(
		loggingModule,
		repositoryModule,
		serviceModule,
		controllerModule,
		routerModule,
		serverModule,
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
