// wire.go
//go:build wireinject
// +build wireinject

package di

import (
	wire_pratice "di-practice/wire_practice"
	"github.com/google/wire"
)

func InitializeBar() *wire_pratice.Bar {
	wire.Build(wire_pratice.NewFoo, wire_pratice.NewBar)
	return &wire_pratice.Bar{}
}
