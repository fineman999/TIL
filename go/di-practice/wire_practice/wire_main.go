package wire_practice

type Foo struct{}

func NewFoo() *Foo {
	return &Foo{}
}

type Bar struct {
	Foo *Foo
}

func NewBar(foo *Foo) *Bar {
	return &Bar{Foo: foo}
}
