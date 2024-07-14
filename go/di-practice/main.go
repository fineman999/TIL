package main

import (
	"di-practice/wire_practice/di"
	"fmt"
)

func main() {
	bar := di.InitializeBar()
	fmt.Println(bar)
}
