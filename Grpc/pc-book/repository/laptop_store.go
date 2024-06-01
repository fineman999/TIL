package repository

import (
	"context"
	"errors"
	"log"
	pb "pc-book/pb/proto"
	"sync"
)

var ErrAlreadyExists = errors.New("record already exists")

type LaptopStore interface {
	Save(laptop *pb.Laptop) error
	Find(id string) (*pb.Laptop, error)
	Search(ctx context.Context, filter *pb.Filter, found func(laptop *pb.Laptop) error) error
}

type InMemoryLaptopStore struct {
	mutex sync.RWMutex
	data  map[string]*pb.Laptop
}

func NewInMemoryLaptopStore() *InMemoryLaptopStore {
	return &InMemoryLaptopStore{
		data: make(map[string]*pb.Laptop),
	}
}

func (store *InMemoryLaptopStore) Save(laptop *pb.Laptop) error {
	store.mutex.Lock()
	defer store.mutex.Unlock()

	if store.data[laptop.Id] != nil {
		return ErrAlreadyExists
	}

	other, err2 := store.deepCopy(laptop)
	if err2 != nil {
		return err2
	}
	store.data[laptop.Id] = other
	return nil
}

func (store *InMemoryLaptopStore) Find(id string) (*pb.Laptop, error) {
	store.mutex.RLock()
	defer store.mutex.RUnlock()

	laptop := store.data[id]
	if laptop == nil {
		return nil, nil
	}

	return laptop, nil
}

func (store *InMemoryLaptopStore) Search(ctx context.Context, filter *pb.Filter, found func(laptop *pb.Laptop) error) error {
	store.mutex.RLock()
	defer store.mutex.RUnlock()

	for _, laptop := range store.data {
		// heavy processing
		//time.Sleep(time.Second)
		//log.Printf("checking laptop id: %s", laptop.GetId())
		if errors.Is(ctx.Err(), context.Canceled) || errors.Is(ctx.Err(), context.DeadlineExceeded) {
			log.Print("context is cancelled")
			return errors.New("search cancelled")
		}
		if isQualified(filter, laptop) {
			other, err := store.deepCopy(laptop)
			if err != nil {
				return err
			}
			err = found(other)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

func isQualified(filter *pb.Filter, laptop *pb.Laptop) bool {
	if laptop.GetPriceUsd() > filter.GetMaxPriceUsd() {
		return false
	}

	if laptop.GetCpu().GetNumberCores() < filter.GetMinCpuCores() {
		return false
	}

	if laptop.GetCpu().GetMinGhz() < filter.GetMinCpuGhz() {
		return false
	}

	//if toBit(laptop.GetMemory()) < toBit(filter.MinRam) {
	//	return false
	//}
	return true
}

func toBit(memory *pb.Memory) uint64 {
	value := memory.GetValue()
	switch memory.GetUnit() {
	case pb.Memory_BIT:
		return value
	case pb.Memory_BYTE:
		return value << 3 // 1 byte = 8 bits
	case pb.Memory_KILOBYTE:
		return value << 13 // 1 KB = 1024 bytes = 8192 bits (1 KB = 1024 * 8 bits) = 2^13 bits
	case pb.Memory_MEGABYTE:
		return value << 23 // 1 MB = 1024 KB = 1024 * 1024 * 8 bits = 2^23 bits
	case pb.Memory_GIGABYTE:
		return value << 33 // 1 GB = 1024 MB = 1024 * 1024 * 1024 bytes = 1024 * 1024 * 1024 * 8 bits = 2^33 bits
	case pb.Memory_TERABYTE:
		return value << 43 // 1 TB = 1024 GB = 1024 * 1024 MB = 1024 * 1024 * 1024 KB = 1024 * 1024 * 1024 * 1024 * 8 bits = 2^43 bits
	default:
		return 0
	}
}

func (store *InMemoryLaptopStore) deepCopy(laptop *pb.Laptop) (*pb.Laptop, error) {
	//other := &pb.Laptop{}
	//err := copier.Copy(other, store.data[laptop.Id])
	//if err != nil {
	//	return nil, fmt.Errorf("cannot copy laptop data: %w", err)
	//}
	return laptop, nil
}
