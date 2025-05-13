package service

import "fx-server/app/repository"

type UserService interface {
	GetAllUsers() ([]repository.User, error)
	GetUser(id int) (repository.User, error)
	CreateUser(name, email string) error
	UpdateUser(id int, name, email string) error
	DeleteUser(id int) error
}

type userService struct {
	userRepo repository.UserRepository
}

func NewUserService(userRepo repository.UserRepository) UserService {
	return &userService{userRepo}
}

func (s *userService) GetAllUsers() ([]repository.User, error) {
	return s.userRepo.FindAll()
}

func (s *userService) GetUser(id int) (repository.User, error) {
	return s.userRepo.FindByID(id)
}

func (s *userService) CreateUser(name, email string) error {
	return s.userRepo.Create(name, email)
}

func (s *userService) UpdateUser(id int, name, email string) error {
	return s.userRepo.Update(id, name, email)
}

func (s *userService) DeleteUser(id int) error {
	return s.userRepo.Delete(id)
}
