package network

import (
	"gemini-ai-service/types"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (n *Network) generateTextWithVertex(g *gin.Context) {
	var req types.TestGeminiRequest

	if err := g.ShouldBindJSON(&req); err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	ctx := g.Request.Context()
	res, err := n.service.GenerateTextWithVertex(ctx, req.Text)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})

}

func (n *Network) generateVideoUrl(g *gin.Context) {
	var req types.VideoUrlRequest

	if err := g.ShouldBindJSON(&req); err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	ctx := g.Request.Context()
	res, err := n.service.GenerateVideoUrl(ctx, req)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})
}
