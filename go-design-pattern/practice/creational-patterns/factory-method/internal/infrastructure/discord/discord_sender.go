package discord

import (
	"context"
	"fmt"
	"github.com/bwmarrin/discordgo"
	"log"
	"practice/creational-patterns/factory-method/config"
	"practice/creational-patterns/factory-method/internal/domain"
)

type Sender struct {
	token          string
	channel        string
	discordSession *discordgo.Session
}

func (s *Sender) SendMessage(ctx context.Context, content string) error {
	_, err := s.discordSession.ChannelMessageSend(s.channel, content)
	if err != nil {
		log.Printf("Discord Send Error: %v", err)
		return err
	}
	return nil
}

func NewDiscordSender(cfg *config.Config) domain.MessageSender {
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
	return &Sender{
		token:          cfg.Discord.Token,
		channel:        cfg.Discord.Channel,
		discordSession: dg,
	}
}
