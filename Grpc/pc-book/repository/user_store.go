package repository

import (
	"pc-book/domain"
	"sync"
)

type UserStore interface {
	Save(user *domain.User) error
	Find(username string) (*domain.User, error)
}

type InMemoryUserStore struct {
	mutex sync.RWMutex
	users map[string]*domain.User
}

func NewInMemoryUserStore() *InMemoryUserStore {
	return &InMemoryUserStore{
		users: make(map[string]*domain.User),
	}
}

func (store *InMemoryUserStore) Save(user *domain.User) error {
	store.mutex.Lock()
	defer store.mutex.Unlock()

	store.users[user.Username] = user.Clone()
	return nil
}

func (store *InMemoryUserStore) Find(username string) (*domain.User, error) {
	store.mutex.RLock()
	defer store.mutex.RUnlock()

	user := store.users[username]
	if user == nil {
		return nil, nil
	}

	return user.Clone(), nil
}
