package repository

import "go-auth-service/config"

type Repository struct {
	cfg *config.Config
}
type User struct {
	UserID string
	Email  string
	Id     string
}

var userRepo = make(map[string]User)

func NewRepository(cfg *config.Config) (*Repository, error) {
	return &Repository{
		cfg: cfg,
	}, nil
}

func (r *Repository) SaveUser(userID, email, id string) {
	userRepo[userID] = User{
		UserID: userID,
		Email:  email,
		Id:     id,
	}
}

func (r *Repository) GetUser(userID string) User {
	return userRepo[userID]
}
