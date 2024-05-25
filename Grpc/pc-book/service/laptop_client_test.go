package service

import (
	"context"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"net"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"testing"
)

func TestClientCreateLaptop(t *testing.T) {
	t.Parallel()

	laptopServer, serverAddress := startTestLaptopServer(t)
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

func startTestLaptopServer(t *testing.T) (*LaptopServer, string) {
	laptopServer := NewLaptopServer(NewInMemoryLaptopStore())

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
