package config

import (
	"context"
	"fmt"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"go.mongodb.org/mongo-driver/mongo/readpref"
	"net/url"
	"time"
)

type Mongo struct {
	Client *mongo.Client
}

func NewMongoConfig(cfg *Config) *Mongo {
	serverAPI := options.ServerAPI(options.ServerAPIVersion1)
	uri := fmt.Sprintf("mongodb+srv://%s:%s@go-oauth-service.qkwrsi0.mongodb.net/?retryWrites=true&w=majority", url.QueryEscape(cfg.Mongo.Username), url.QueryEscape(cfg.Mongo.Password))
	clientOptions := options.Client().ApplyURI(uri).SetServerAPIOptions(serverAPI)

	client, err := mongo.Connect(context.Background(), clientOptions)
	if err != nil {
		panic(err)
	}
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()
	err = client.Ping(ctx, readpref.Primary())
	if err != nil {
		panic(err)
	}

	return &Mongo{Client: client}
}

func (m *Mongo) Close() {
	if err := m.Client.Disconnect(context.Background()); err != nil {
		panic(err)
	}
}
