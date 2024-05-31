package network

import (
	"github.com/gin-gonic/gin"
	"github.com/slack-go/slack"
	"net/http"
)

func (n *Network) chatSlackWithAI(g *gin.Context) {
	parse, err := slack.SlashCommandParse(g.Request)
	if err != nil {
		g.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	ctx := g.Request.Context()
	text, err := n.service.SendSlackWithAI(ctx, parse)
	if err != nil {
		g.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	g.JSON(http.StatusOK, gin.H{"response": text})
}
