package gcs

import (
	"cloud.google.com/go/storage"
	"context"
	"fmt"
	"gemini-ai-service/config"
	"gemini-ai-service/types"
	"github.com/google/uuid"
	"io"
	"log"
	"mime/multipart"
	"path/filepath"
)

type Storage struct {
	client     *storage.Client
	bucket     *storage.BucketHandle
	bucketName string
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
		cfg.GCS.Bucket,
		cfg.GCS.ProjectID,
		cfg.GCS.UploadPath,
	}, nil
}

func (s *Storage) UploadFile(ctx context.Context, objectName string, file multipart.File) (*types.FileToVertex, error) {
	// Upload an object with storage.Writer.
	// TODO: objectName은 추후 디비가 생기면 저장
	uuidName := uuid.New().String() + filepath.Ext(objectName)

	wc := s.bucket.Object(s.uploadPath + uuidName).NewWriter(ctx)
	if _, err := io.Copy(wc, file); err != nil {
		return nil, fmt.Errorf("io.Copy: %v", err)
	}
	if err := wc.Close(); err != nil {
		return nil, fmt.Errorf("Writer.Close: %v", err)
	}

	res := &types.FileToVertex{
		BucketName: s.bucketName,
		FileName:   uuidName,
		Path:       s.uploadPath + uuidName,
	}
	return res, nil
}

func (s *Storage) DeleteFile(ctx context.Context, objectName string) error {
	o := s.bucket.Object(s.uploadPath + objectName)
	if err := o.Delete(ctx); err != nil {
		return fmt.Errorf("Object(%q).Delete: %v", objectName, err)
	}
	return nil
}

func (s *Storage) Close() {
	log.Println("Closing GCS client")
	s.client.Close()
}
