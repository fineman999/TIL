package sample

import (
	"github.com/google/uuid"
	"math/rand"
	pb "pc-book/pb/proto"
	"time"
)

func init() {
	rand.NewSource(time.Now().UnixNano())
}

func randomKeyboardLayout() pb.Keyboard_Layout {
	switch rand.Intn(3) {
	case 1:
		return pb.Keyboard_QWERTY
	case 2:
		return pb.Keyboard_QWERTZ
	case 3:
		return pb.Keyboard_AZERTY
	}
	return pb.Keyboard_UNKNOWN
}

func randomBool() bool {
	return rand.Intn(2) == 1
}

func randomCPUBrand() string {
	return randomStringFromSet("Intel", "AMD")
}

func randomStringFromSet(a ...string) string {
	n := len(a)
	if n == 0 {
		return ""
	}
	return a[rand.Intn(n)]
}

func randomCPUName(brand string) string {
	if brand == "Intel" {
		return randomStringFromSet(
			"Core i9-9900K",
			"Core i7-8700K",
			"Core i5-9600K",
			"Core i3-9100",
			"Xeon E-2286M")
	}
	return randomStringFromSet(
		"Ryzen 9 3900X",
		"Ryzen 7 3700X",
		"Ryzen 5 3600X",
	)
}

func randomInt(min, max int) int {
	return min + rand.Intn(max-min+1)
}

func randomFloat(min, max float64) float64 {
	return min + rand.Float64()*(max-min)
}

func randomGPUBrand() string {
	return randomStringFromSet("Nvidia", "AMD")
}

func randomGPUName(brand string) string {
	if brand == "Nvidia" {
		return randomStringFromSet(
			"RTX 2080 Ti",
			"RTX 2070",
			"GTX 1660 Ti",
		)
	}
	return randomStringFromSet(
		"RX 5700 XT",
		"RX 5600 XT",
		"RX 5500 XT",
	)
}

func randomFloat32(min, max float32) float32 {
	return min + rand.Float32()*(max-min)
}

func randomFloat64(min, max float64) float64 {
	return min + rand.Float64()*(max-min)
}

func randomScreenPanel() pb.Screen_Panel {
	if rand.Intn(2) == 1 {
		return pb.Screen_LCD
	}
	return pb.Screen_OLED
}

func randomScreenResolution() *pb.Screen_Resolution {
	height := randomInt(1080, 4320)
	width := height * 16 / 9
	return &pb.Screen_Resolution{
		Width:  uint32(width),
		Height: uint32(height),
	}
}

func randomID() string {
	return uuid.New().String()
}

func randomLaptopBrand() string {
	return randomStringFromSet("Apple", "Dell", "Lenovo")
}

func randomLaptopName(brand string) string {
	if brand == "Apple" {
		return randomStringFromSet("MacBook Pro", "MacBook Air")
	}
	if brand == "Dell" {
		return randomStringFromSet("Latitude", "Vostro", "XPS")
	}
	return randomStringFromSet("ThinkPad X1", "ThinkPad P1")
}
