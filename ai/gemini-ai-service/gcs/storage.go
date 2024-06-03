package gcs

import (
	"cloud.google.com/go/storage"
	"context"
	"fmt"
	"gemini-ai-service/config"
	"io"
	"log"
	"mime/multipart"
)

type Storage struct {
	client     *storage.Client
	bucket     *storage.BucketHandle
	project    string
	uploadPath string
}

func NewStorage(cfg *config.Config) (*Storage, error) {
	ctx := context.Background()
	// Sets your Google Cloud Platform project ID.

	// Creates a client.
	client, err := storage.NewClient(ctx)
	if err != nil {
		log.Fatalf("Failed to create client: %v", err)
	}

	bucket := client.Bucket(cfg.GCS.Bucket)
	return &Storage{
		client,
		bucket,
		cfg.GCS.ProjectID,
		cfg.GCS.UploadPath,
	}, nil
}

func (s *Storage) UploadFile(ctx context.Context, objectName string, file multipart.File) (string, error) {
	// Upload an object with storage.Writer.
	wc := s.bucket.Object(s.uploadPath + objectName).NewWriter(ctx)
	if _, err := io.Copy(wc, file); err != nil {
		return "", fmt.Errorf("io.Copy: %v", err)
	}
	if err := wc.Close(); err != nil {
		return "", fmt.Errorf("Writer.Close: %v", err)
	}

	return wc.Name, nil
}

func (s *Storage) Close() {
	log.Println("Closing GCS client")
	s.client.Close()
}
