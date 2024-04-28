package network

import (
	"github.com/gin-gonic/gin"
	"rpc-server/config"
	"rpc-server/service"
)

type Network struct {
	cfg     *config.Config
	service *service.Service
	engin   *gin.Engine
}

func NewNetwork(cfg *config.Config, service *service.Service) (*Network, error) {
	n := &Network{cfg: cfg, service: service, engin: gin.Default()}
	return n, nil
}

func (n *Network) StartServer() {
	err := n.engin.Run(":8080")
	if err != nil {
		panic(err)
	}
}
