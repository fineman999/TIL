package di

import (
	"context"
	"fmt"
	"fx-server/config"
	"github.com/redis/go-redis/v9"
	"go.uber.org/fx"
)

type MySqlClient struct {
	EntClient *ent.Client
}

type RedisStore struct {
	RedisClient *redis.Client
}

var ExternalServicesModule = fx.Module("external-services",
	fx.Provide(
		// MySQL 클라이언트
		func(cfg *config.Config) (*MySqlClient, error) {
			client, err := ent.Open("mysql", "user:password@tcp(localhost:3306)/dbname?parseTime=true")
			if err != nil {
				return nil, fmt.Errorf("failed to connect to MySQL: %v", err)
			}
			return &MySqlClient{EntClient: client}, nil
		},
		// Redis 클라이언트
		func(cfg *config.Config) (*RedisStore, error) {
			client := redis.NewClient(&redis.Options{
				Addr: "localhost:6379",
			})
			if err := client.Ping(context.Background()).Err(); err != nil {
				return nil, fmt.Errorf("failed to connect to Redis: %v", err)
			}
			return &RedisStore{RedisClient: client}, nil
		},
	),
	fx.Invoke(func(lc fx.Lifecycle, mysql *MySqlClient, redis *RedisStore) {
		lc.Append(fx.Hook{
			OnStop: func(ctx context.Context) error {
				if err := mysql.EntClient.Close(); err != nil {
					fmt.Printf("Failed to close MySQL: %v\n", err)
				}
				if err := redis.RedisClient.Close(); err != nil {
					fmt.Printf("Failed to close Redis: %v\n", err)
				}
				return nil
			},
		})
	}),
)
