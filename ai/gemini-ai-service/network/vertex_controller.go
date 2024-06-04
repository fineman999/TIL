package network

import (
	"encoding/json"
	"gemini-ai-service/types"
	"github.com/gin-gonic/gin"
	"net/http"
)

func (n *Network) generateTextWithVertex(g *gin.Context) {
	var req types.TextRequest

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

func (n *Network) generateFile(g *gin.Context) {
	file, header, err := g.Request.FormFile("file")
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	value := g.Request.FormValue("text")
	var req types.TextRequest
	err = json.Unmarshal([]byte(value), &req)
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	ctx := g.Request.Context()
	res, err := n.service.GenerateFile(ctx, file, header, req.Text)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})
}
