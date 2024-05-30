package network

import (
	"context"
	"gemini-ai-service/ai"
	"gemini-ai-service/config"
	"gemini-ai-service/service"
	"github.com/gin-gonic/gin"
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
	ai      *ai.Gemini
}

func NewNetwork(cfg *config.Config, service *service.Service) (*Network, error) {
	r := &Network{cfg: cfg, service: service, router: gin.Default()}

	r.router.POST("/test", r.test)
	r.router.POST("/chat/rooms/:id", r.createChatRoom)
	r.router.POST("/chat/rooms/:id/text", r.sendChatText)
	r.router.POST("/image", r.imageTest)
	return r, nil
}

func (n *Network) StartServer(ai *ai.Gemini) {
	n.ai = ai
	//n.router.Run(":8080")
	srv := &http.Server{
		Addr:    ":8080",
		Handler: n.router,
	}
	n.router.MaxMultipartMemory = 8 << 20 // 8 MiB 제한
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
	n.ai.Close()

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
