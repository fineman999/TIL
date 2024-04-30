package network

import (
	"github.com/gin-gonic/gin"
	"net/http"
	"rpc-server/types"
)

func (n *Network) login(g *gin.Context) {
	var req types.LoginReq

	if err := g.ShouldBindJSON(&req); err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	res, err := n.service.CreateAuth(req.Name)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})

}

func (n *Network) verify(g *gin.Context) {
	g.JSON(http.StatusOK, gin.H{"message": "success"})
}
