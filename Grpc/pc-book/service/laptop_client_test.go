package service

import (
	"context"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"io"
	"net"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"testing"
)

func TestClientCreateLaptop(t *testing.T) {
	t.Parallel()

	laptopServer, serverAddress := startTestLaptopServer(t, NewInMemoryLaptopStore())
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
	other, err := laptopServer.Store.Find(expectedID)
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

		expectedIDs[laptop.Id] = true

		err := store.Save(laptop)
		require.NoError(t, err)
	}

	_, serverAddress := startTestLaptopServer(t, store)
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

func startTestLaptopServer(t *testing.T, store *InMemoryLaptopStore) (*LaptopServer, string) {
	laptopServer := NewLaptopServer(store)

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
