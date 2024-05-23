package sample

import (
	"google.golang.org/protobuf/types/known/timestamppb"
	pb "pc-book/pb/proto"
)

func NewKeyboard() *pb.Keyboard {
	keyboard := &pb.Keyboard{
		Layout:  randomKeyboardLayout(),
		Backlit: randomBool(),
	}
	return keyboard
}

func NewCPU() *pb.CPU {
	brand := randomCPUBrand()

	numberCores := randomInt(2, 8)
	numberThreads := randomInt(2, 16)
	minGhz := randomFloat(2.0, 3.5)
	maxGhz := randomFloat(minGhz, 5.0)
	cpu := &pb.CPU{
		Brand:         brand,
		Name:          randomCPUName(brand),
		NumberCores:   uint32(numberCores),
		NumberThreads: uint32(numberThreads),
		MinGhz:        minGhz,
		MaxGhz:        maxGhz,
	}
	return cpu
}

func NewGPU() *pb.GPU {
	brand := randomGPUBrand()

	minGhz := randomFloat(1.0, 1.5)
	maxGhz := randomFloat(minGhz, 2.0)

	memory := &pb.Memory{
		Value: uint64(randomInt(2, 6)),
		Unit:  pb.Memory_GIGABYTE,
	}
	gpu := &pb.GPU{
		Brand:  brand,
		Name:   randomGPUName(brand),
		MinGhz: minGhz,
		MaxGhz: maxGhz,
		Memory: memory,
	}
	return gpu
}

//func NewRAM() *pb.Memory {
//	ram := &pb.Memory{
//		Value: uint64(randomInt(4, 64)),
//		Unit:  pb.Memory_GIGABYTE,
//	}
//	return ram
//}

func NewSSD() *pb.Storage {
	ssd := &pb.Storage{
		Driver: pb.Storage_SSD,
		Memory: &pb.Memory{
			Value: uint64(randomInt(128, 1024)),
			Unit:  pb.Memory_GIGABYTE,
		},
	}
	return ssd
}

func NewHDD() *pb.Storage {
	hdd := &pb.Storage{
		Driver: pb.Storage_HDD,
		Memory: &pb.Memory{
			Value: uint64(randomInt(1, 6)),
			Unit:  pb.Memory_TERABYTE,
		},
	}
	return hdd
}

func NewScreen() *pb.Screen {
	screen := &pb.Screen{
		SizeInch:   randomFloat32(13, 17),
		Resolution: randomScreenResolution(),
		Panel:      randomScreenPanel(),
		Multitouch: randomBool(),
	}
	return screen
}

func NewLaptop() *pb.Laptop {
	brand := randomLaptopBrand()
	name := randomLaptopName(brand)

	laptop := &pb.Laptop{
		Id:    randomID(),
		Brand: brand,
		Name:  name,
		Cpu:   NewCPU(),
		Gpu:   []*pb.GPU{NewGPU()},
		Storage: []*pb.Storage{
			NewSSD(),
			NewHDD(),
		},
		Screen:   NewScreen(),
		Keyboard: NewKeyboard(),
		Weight: &pb.Laptop_WeightKg{
			WeightKg: randomFloat64(1.0, 3.0),
		},
		PriceUsd:    randomFloat64(1500, 3000),
		ReleaseYear: uint32(randomInt(2015, 2021)),
		UpdatedAt:   timestamppb.Now(),
	}
	return laptop
}
