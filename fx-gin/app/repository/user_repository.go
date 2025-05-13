package repository

import (
	"errors"
	"sync"
)

type User struct {
	ID    int    `json:"id"`
	Name  string `json:"name"`
	Email string `json:"email"`
}

type UserRepository interface {
	FindAll() ([]User, error)
	FindByID(id int) (User, error)
	Create(name, email string) error
	Update(id int, name, email string) error
	Delete(id int) error
}

type userRepository struct {
	users map[int]User
	mu    sync.RWMutex
}

func NewUserRepository() UserRepository {
	return &userRepository{
		users: make(map[int]User),
	}
}

func (r *userRepository) FindAll() ([]User, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	var users []User
	for _, user := range r.users {
		users = append(users, user)
	}
	return users, nil
}

func (r *userRepository) FindByID(id int) (User, error) {
	r.mu.RLock()
	defer r.mu.RUnlock()
	user, exists := r.users[id]
	if !exists {
		return User{}, errors.New("user not found")
	}
	return user, nil
}

func (r *userRepository) Create(name, email string) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	id := len(r.users) + 1
	r.users[id] = User{ID: id, Name: name, Email: email}
	return nil
}

func (r *userRepository) Update(id int, name, email string) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if _, exists := r.users[id]; !exists {
		return errors.New("user not found")
	}
	r.users[id] = User{ID: id, Name: name, Email: email}
	return nil
}

func (r *userRepository) Delete(id int) error {
	r.mu.Lock()
	defer r.mu.Unlock()
	if _, exists := r.users[id]; !exists {
		return errors.New("user not found")
	}
	delete(r.users, id)
	return nil
}
