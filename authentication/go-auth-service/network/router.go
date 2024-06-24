package network

import (
	"context"
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
	r := &Network{cfg: cfg, service: service, router: gin.Default()}
	testGroup := r.router.Group("/api/test")
	testGroup.GET("", r.test)

	oauthGroup := r.router.Group("/login/oauth2/code")
	oauthGroup.GET("/twitter", r.twitterOAuth)
	oauthGroup.GET("/google", r.googleOAuth)

	pkceGroup := r.router.Group("/api/pkce")
	pkceGroup.GET("", r.getPkceInfo)
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
