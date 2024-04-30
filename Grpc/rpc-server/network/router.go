package network

import (
	"github.com/gin-gonic/gin"
	"rpc-server/config"
	"rpc-server/gRPC/client"
	"rpc-server/service"
)

type Network struct {
	cfg        *config.Config
	service    *service.Service
	engin      *gin.Engine
	gRPCClient *client.GRPCClient
}

func NewNetwork(cfg *config.Config, service *service.Service, client *client.GRPCClient) (*Network, error) {
	n := &Network{cfg: cfg, service: service, engin: gin.Default(), gRPCClient: client}
	return n, nil
}

func (n *Network) StartServer() {
	err := n.engin.Run(":8080")
	if err != nil {
		panic(err)
	}
}
