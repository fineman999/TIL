package network

import (
	"gemini-ai-service/types"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

func (n *Network) test(g *gin.Context) {
	var req types.TestGeminiRequest

	if err := g.ShouldBindJSON(&req); err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	ctx := g.Request.Context()
	res, err := n.service.TestGemini(ctx, req.Text)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})

}

func (n *Network) createChatRoom(g *gin.Context) {

	id, err := strconv.Atoi(g.Param("id"))
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	//ctx := g.Request.Context()
	n.service.CreateChat(id)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": "success"})
}

func (n *Network) sendChatText(g *gin.Context) {
	var req types.ChatTestGeminiRequest

	if err := g.ShouldBindJSON(&req); err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	id, err := strconv.Atoi(g.Param("id"))
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	ctx := g.Request.Context()
	res, err := n.service.TestChat(ctx, &req, id)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": res})
}
