package paseto

import (
	"crypto/rand"
	"github.com/o1egl/paseto"
	"rpc-server/config"
	auth "rpc-server/gRPC/proto"
)

type PasetoMaker struct {
	Pt  *paseto.V2
	Key []byte
}

func NewPasetoMaker(cfg *config.Config) *PasetoMaker {
	return &PasetoMaker{
		Pt:  paseto.NewV2(),
		Key: []byte(cfg.Paseto.Key),
	}
}

func (m *PasetoMaker) CreateToken(a *auth.AuthData) (string, error) {
	randomBytes := make([]byte, 16)
	_, err := rand.Read(randomBytes)
	if err != nil {
		return "", err
	}
	return m.Pt.Encrypt(m.Key, a, randomBytes)
}

func (m *PasetoMaker) VerifyToken(token string) error {
	var a *auth.AuthData
	return m.Pt.Decrypt(token, m.Key, a, nil)
}
