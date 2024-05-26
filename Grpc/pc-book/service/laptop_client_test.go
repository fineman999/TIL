package service

import (
	"bufio"
	"context"
	"errors"
	"fmt"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"io"
	"net"
	"os"
	"path/filepath"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"testing"
)

func TestClientUploadImage(t *testing.T) {
	t.Parallel()

	testImageFolder := "../tmp"

	laptopStore := NewInMemoryLaptopStore()
	imageStore := NewDiskImageStore(testImageFolder)

	laptop := sample.NewLaptop()
	err := laptopStore.Save(laptop)
	require.NoError(t, err)

	_, serverAddress := startTestLaptopServer(t, laptopStore, imageStore)
	laptopClient, conn := newTestLaptopClient(t, serverAddress)
	defer conn.Close()

	imagePath := fmt.Sprintf("%s/laptop.jpg", testImageFolder)
	file, err := os.Open(imagePath)
	require.NoError(t, err)
	defer file.Close()

	stream, err := laptopClient.UploadImage(context.Background())
	require.NoError(t, err)

	req := &pb.UploadImageRequest{
		Data: &pb.UploadImageRequest_Info{
			Info: &pb.ImageInfo{
				LaptopId:  laptop.GetId(),
				ImageType: filepath.Ext(imagePath),
			},
		},
	}

	err = stream.Send(req)
	require.NoError(t, err)

	reader := bufio.NewReader(file)
	buffer := make([]byte, 1024)
	size := 0
	for {
		// buffer: 읽은 데이터를 저장할 공간
		// n: 실제로 읽은 데이터의 크기
		n, err := reader.Read(buffer)
		if err != nil {
			if errors.Is(err, io.EOF) {
				break
			}
			require.NoError(t, err)
		}
		size += n

		req := &pb.UploadImageRequest{
			Data: &pb.UploadImageRequest_ChunkData{
				// buffer[:n]: buffer의 0번째부터 n-1번째까지의 데이터
				ChunkData: buffer[:n],
			},
		}

		err = stream.Send(req)
		require.NoError(t, err)
	}

	res, err := stream.CloseAndRecv()
	require.NoError(t, err)
	require.NotZero(t, res.GetId())
	require.EqualValues(t, size, res.GetSize())

	savedImagePath := fmt.Sprintf("%s/%s%s", testImageFolder, res.GetId(), filepath.Ext(imagePath))
	require.FileExists(t, savedImagePath)
	require.NoError(t, os.Remove(savedImagePath))
}

func TestClientCreateLaptop(t *testing.T) {
	t.Parallel()

	laptopServer, serverAddress := startTestLaptopServer(t, NewInMemoryLaptopStore(), nil)
	laptopClient, conn := newTestLaptopClient(t, serverAddress)
	defer conn.Close()

	laptop := sample.NewLaptop()
	expectedID := laptop.Id
	req := &pb.CreateLaptopRequest{Laptop: laptop}

	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	res, err := laptopClient.CreateLaptop(ctx, req)
	require.NoError(t, err)
	require.NotNil(t, res)
	require.Equal(t, expectedID, res.Id)

	// Check if the laptop is saved to the store
	other, err := laptopServer.laptopStore.Find(expectedID)
	requireSampleLaptop(t, other, expectedID)

}

func newTestLaptopClient(t *testing.T, address string) (pb.LaptopServiceClient, *grpc.ClientConn) {
	// grpc.WithTransportCredentials(insecure.NewCredentials()): 인증 없이 연결 -> 테스트용
	conn, err := grpc.NewClient(address, grpc.WithTransportCredentials(insecure.NewCredentials()))
	require.NoError(t, err)

	// pb.NewLaptopServiceClient(conn): gRPC 클라이언트 생성
	client := pb.NewLaptopServiceClient(conn)
	require.NotNil(t, client)
	return client, conn
}

func TestClientSearchLaptop(t *testing.T) {
	t.Parallel()

	filter := &pb.Filter{
		MaxPriceUsd: 3000,
		MinCpuCores: 4,
		MinCpuGhz:   2.0,
		MinRam:      &pb.Memory{Value: 8, Unit: pb.Memory_GIGABYTE},
	}

	store := NewInMemoryLaptopStore()
	expectedIDs := make(map[string]bool)

	for i := 0; i < 6; i++ {
		laptop := sample.NewLaptop()
		laptop.PriceUsd = 2000
		laptop.Cpu.NumberCores = 5
		laptop.Cpu.MinGhz = 3.0

		expectedIDs[laptop.Id] = true

		err := store.Save(laptop)
		require.NoError(t, err)
	}

	_, serverAddress := startTestLaptopServer(t, store, nil)
	laptopClient, conn := newTestLaptopClient(t, serverAddress)
	defer conn.Close()

	req := &pb.SearchLaptopRequest{Filter: filter}
	stream, err := laptopClient.SearchLaptop(context.Background(), req)
	require.NoError(t, err)

	found := 0
	for {
		res, err := stream.Recv()
		if err == io.EOF {
			break
		}

		require.NoError(t, err)
		require.Contains(t, expectedIDs, res.Laptop.Id)

		found++

	}

	require.Equal(t, len(expectedIDs), found)
}

func startTestLaptopServer(t *testing.T, store *InMemoryLaptopStore, imageStore ImageStore) (*LaptopServer, string) {
	laptopServer := NewLaptopServer(store, imageStore)

	grpcServer := grpc.NewServer()
	pb.RegisterLaptopServiceServer(grpcServer, laptopServer)

	listener, err := net.Listen("tcp", ":0") // :0 means random port
	require.NoError(t, err)

	go func() {
		err := grpcServer.Serve(listener)
		require.NoError(t, err)
	}()

	return laptopServer, listener.Addr().String()
}

func requireSampleLaptop(t *testing.T, laptop *pb.Laptop, expectedID string) {
	require.NotNil(t, laptop)
	require.NotEmpty(t, laptop.Id)
	require.Equal(t, expectedID, laptop.Id)
}
