package network

import (
	"context"
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"go-auth-service/config"
	"go-auth-service/service"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"
)

type Network struct {
	cfg     *config.Config
	service *service.Service
	router  *gin.Engine
	srv     *http.Server
}

func NewNetwork(cfg *config.Config, service *service.Service) (*Network, error) {
	engine := gin.Default()
	corsConfig := cors.DefaultConfig()
	corsConfig.AllowOrigins = []string{
		"http://localhost:3000",
		"http://127.0.0.1:3000",
	}
	corsConfig.AllowCredentials = true
	corsConfig.AllowMethods = []string{"GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"}
	corsConfig.AllowHeaders = []string{"Content-Type", "Content-Length", "Accept-Encoding", "X-CSRF-Token", "X-Forwarded-For", "Authorization", "accept", "origin", "Cache-Control", "X-Requested-With", "ip"}
	engine.Use(cors.New(corsConfig))

	r := &Network{cfg: cfg, service: service, router: engine}
	testGroup := r.router.Group("/api/test")
	testGroup.GET("", r.test)

	oauthGroup := r.router.Group("/login/oauth2/code")
	oauthGroup.GET("/twitter", r.twitterOAuth)
	oauthGroup.GET("/google", r.googleOAuth)

	pkceGroup := r.router.Group("/api/pkce")
	pkceGroup.GET("", r.getPkceInfo)

	oauth1Group := r.router.Group("/api/oauth1")
	oauth1Group.GET("/twitter", r.startOauth1)

	oauth1CallbackGroup := r.router.Group("/login/oauth1/code")
	oauth1CallbackGroup.GET("/twitter", r.callbackOAuth1)
	return r, nil
}

func (n *Network) StartServer(port string) {

	srv := &http.Server{
		Addr:    ":" + port,
		Handler: n.router,
	}

	n.srv = srv

	go func() {
		// service connections
		if err := n.srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("listen: %s\n", err)
		}
	}()
	n.endServer()
}

func (n *Network) endServer() {
	// Wait for interrupt signal to gracefully shutdown the server with
	// a timeout of 5 seconds.
	quit := make(chan os.Signal, 1)
	// kill (no param) default send syscall.SIGTERM
	// kill -2 is syscall.SIGINT
	// kill -9 is syscall. SIGKILL but can"t be catch, so don't need add it
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Shutdown Server ...")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := n.srv.Shutdown(ctx); err != nil {
		log.Fatal("Server Shutdown:", err)
	}
	// catching ctx.Done(). timeout of 5 seconds.
	select {
	case <-ctx.Done():
		log.Println("timeout of 5 seconds.")
	}
	log.Println("Server exiting")
}
