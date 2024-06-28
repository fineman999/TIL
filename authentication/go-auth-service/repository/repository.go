package repository

import (
	"context"
	"go-auth-service/config"
	"go.mongodb.org/mongo-driver/mongo"
)

type Repository struct {
	cfg        *config.Config
	mongo      *config.Mongo
	collection *mongo.Collection
}
type User struct {
	UserID string
	Email  string
	Id     string
}

func NewRepository(cfg *config.Config, mongo *config.Mongo) (*Repository, error) {
	collection := mongo.Client.Database("go-oauth-service").Collection("users")
	return &Repository{
		cfg:        cfg,
		mongo:      mongo,
		collection: collection,
	}, nil
}

func (r *Repository) SaveUser(ctx context.Context, userID, email, id string) (*mongo.InsertOneResult, error) {
	return r.collection.InsertOne(ctx, User{
		UserID: userID,
		Email:  email,
		Id:     id,
	})
}

func (r *Repository) GetUser(ctx context.Context, userID string) (*User, error) {
	var user User
	err := r.collection.FindOne(ctx, User{UserID: userID}).Decode(&user)
	return &user, err
}
