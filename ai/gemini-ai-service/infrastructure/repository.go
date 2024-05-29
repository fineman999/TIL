package infrastructure

import (
	"errors"
	"gemini-ai-service/config"
)

const (
	USER  = "user"
	MODEL = "model"
)

type Type string
type Room struct {
	Type Type
	Text string
}
type Repository struct {
	cfg  *config.Config
	room map[int][]Room
}

func NewRepository(cfg *config.Config) (*Repository, error) {
	return &Repository{
		cfg:  cfg,
		room: make(map[int][]Room),
	}, nil
}

func (r *Repository) AddRoom(id int, room Room) {
	r.room[id] = append(r.room[id], room)
}

func (r *Repository) GetRoomByID(id int) ([]Room, error) {
	rooms, exists := r.room[id]
	if !exists {
		return nil, errors.New("room not found")
	}
	return rooms, nil
}
