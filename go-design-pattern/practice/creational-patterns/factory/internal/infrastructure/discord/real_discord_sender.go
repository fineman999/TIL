package discord

import (
	"context"
	"fmt"
	"github.com/bwmarrin/discordgo"
	"log"
	"practice/creational-patterns/factory/config"
	"practice/creational-patterns/factory/internal/domain"
)

type RealSender struct {
	token          string
	channel        string
	discordSession *discordgo.Session
}

func (s *RealSender) SendMessage(ctx context.Context, content string) error {
	_, err := s.discordSession.ChannelMessageSend(s.channel, content)
	if err != nil {
		log.Printf("Discord Send Error: %v", err)
		return err
	}
	return nil
}

func NewRealSender(cfg *config.Config) domain.MessageSender {
	dg, err := discordgo.New("Bot " + cfg.Discord.Token)
	if err != nil {
		fmt.Println("Error creating Discord session:", err)
		return nil
	}

	// 디스코드 연결
	err = dg.Open()
	if err != nil {
		fmt.Println("Error opening connection:", err)
		return nil
	}
	return &RealSender{
		token:          cfg.Discord.Token,
		channel:        cfg.Discord.Channel,
		discordSession: dg,
	}
}
