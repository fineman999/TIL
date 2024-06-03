package network

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

func (n *Network) testUploadFile(g *gin.Context) {
	file, header, err := g.Request.FormFile("file")
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	ctx := g.Request.Context()
	res, err := n.service.UploadFile(ctx, file, header)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})
}
