package service

import (
	"context"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	pb "pc-book/pb/proto"
	"pc-book/sample"
	"reflect"
	"testing"
)

func TestServerCreateLaptop(t *testing.T) {
	t.Parallel()

	laptopNoID := sample.NewLaptop()
	laptopNoID.Id = ""

	laptopInvalidID := sample.NewLaptop()
	laptopInvalidID.Id = "invalid-uuid"

	laptopDuplicateID := sample.NewLaptop()
	storeDuplicateID := NewInMemoryLaptopStore()
	err := storeDuplicateID.Save(laptopDuplicateID)
	require.Nil(t, err)

	testCases := []struct {
		name   string
		laptop *pb.Laptop
		store  LaptopStore
		code   codes.Code
	}{
		{
			name:   "success_with_id",
			laptop: sample.NewLaptop(),
			store:  NewInMemoryLaptopStore(),
			code:   codes.OK,
		},
		{
			name:   "success_no_id",
			laptop: laptopNoID,
			store:  NewInMemoryLaptopStore(),
			code:   codes.OK,
		},
		{
			name:   "failure_invalid_id",
			laptop: laptopInvalidID,
			store:  NewInMemoryLaptopStore(),
			code:   codes.InvalidArgument,
		},
		{
			name:   "failure_duplicate_id",
			laptop: laptopDuplicateID,
			store:  storeDuplicateID,
			code:   codes.AlreadyExists,
		},
	}

	for i := range testCases {
		tc := testCases[i]
		t.Run(tc.name, func(t *testing.T) {
			t.Parallel()
			server := NewLaptopServer(tc.store, nil)
			req := &pb.CreateLaptopRequest{
				Laptop: tc.laptop,
			}

			ctx, cancel := context.WithCancel(context.Background())
			defer cancel()
			res, err := server.CreateLaptop(ctx, req)
			if tc.code == codes.OK {
				require.NoError(t, err)
				require.NotNil(t, res)
				require.NotEmpty(t, res.Id)
				if len(tc.laptop.Id) > 0 {
					require.Equal(t, tc.laptop.Id, res.Id)
				}
			} else {
				require.Error(t, err)
				st, ok := status.FromError(err)
				require.True(t, ok)
				require.Equal(t, tc.code, st.Code())
			}
		})
	}

}

func TestLaptopServer_CreateLaptop(t *testing.T) {
	type fields struct {
		LaptopServiceServer pb.LaptopServiceServer
		laptopStore         LaptopStore
		imageStore          ImageStore
	}
	type args struct {
		ctx context.Context
		req *pb.CreateLaptopRequest
	}
	var tests []struct {
		name    string
		fields  fields
		args    args
		want    *pb.CreateLaptopResponse
		wantErr bool
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			server := &LaptopServer{
				LaptopServiceServer: tt.fields.LaptopServiceServer,
				laptopStore:         tt.fields.laptopStore,
				imageStore:          tt.fields.imageStore,
			}
			got, err := server.CreateLaptop(tt.args.ctx, tt.args.req)
			if (err != nil) != tt.wantErr {
				t.Errorf("CreateLaptop() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(got, tt.want) {
				t.Errorf("CreateLaptop() got = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestLaptopServer_SearchLaptop(t *testing.T) {
	type fields struct {
		LaptopServiceServer pb.LaptopServiceServer
		laptopStore         LaptopStore
		imageStore          ImageStore
	}
	type args struct {
		req    *pb.SearchLaptopRequest
		stream pb.LaptopService_SearchLaptopServer
	}
	var tests []struct {
		name    string
		fields  fields
		args    args
		wantErr bool
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			server := &LaptopServer{
				LaptopServiceServer: tt.fields.LaptopServiceServer,
				laptopStore:         tt.fields.laptopStore,
				imageStore:          tt.fields.imageStore,
			}
			if err := server.SearchLaptop(tt.args.req, tt.args.stream); (err != nil) != tt.wantErr {
				t.Errorf("SearchLaptop() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestLaptopServer_checkContextError(t *testing.T) {
	type fields struct {
		LaptopServiceServer pb.LaptopServiceServer
		laptopStore         LaptopStore
		imageStore          ImageStore
	}
	type args struct {
		ctx context.Context
	}
	var tests []struct {
		name    string
		fields  fields
		args    args
		wantErr bool
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			server := &LaptopServer{
				LaptopServiceServer: tt.fields.LaptopServiceServer,
				laptopStore:         tt.fields.laptopStore,
				imageStore:          tt.fields.imageStore,
			}
			if err := server.checkContextError(tt.args.ctx); (err != nil) != tt.wantErr {
				t.Errorf("checkContextError() error = %v, wantErr %v", err, tt.wantErr)
			}
		})
	}
}

func TestNewLaptopServer(t *testing.T) {
	type args struct {
		laptopStore LaptopStore
		imageStore  ImageStore
	}
	var tests []struct {
		name string
		args args
		want *LaptopServer
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := NewLaptopServer(tt.args.laptopStore, tt.args.imageStore); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("NewLaptopServer() = %v, want %v", got, tt.want)
			}
		})
	}
}
