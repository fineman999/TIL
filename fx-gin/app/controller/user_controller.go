package controller

import (
	"fx-server/app/service"
	"github.com/gin-gonic/gin"
	"net/http"
	"strconv"
)

type UserController struct {
	userService service.UserService
}

func NewUserController(userService service.UserService) *UserController {
	return &UserController{userService}
}

type User struct {
	ID    int    `json:"id"`
	Name  string `json:"name"`
	Email string `json:"email"`
}

func (c *UserController) GetAllUsers(ctx *gin.Context) {
	users, err := c.userService.GetAllUsers()
	if err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	ctx.JSON(http.StatusOK, users)
}

func (c *UserController) GetUser(ctx *gin.Context) {
	id, _ := strconv.Atoi(ctx.Param("id"))
	user, err := c.userService.GetUser(id)
	if err != nil {
		ctx.JSON(http.StatusNotFound, gin.H{"error": "User not found"})
		return
	}
	ctx.JSON(http.StatusOK, user)
}

func (c *UserController) CreateUser(ctx *gin.Context) {
	var user User
	if err := ctx.ShouldBindJSON(&user); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if err := c.userService.CreateUser(user.Name, user.Email); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	ctx.JSON(http.StatusCreated, gin.H{"message": "User created"})
}

func (c *UserController) UpdateUser(ctx *gin.Context) {
	id, _ := strconv.Atoi(ctx.Param("id"))
	var user User
	if err := ctx.ShouldBindJSON(&user); err != nil {
		ctx.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if err := c.userService.UpdateUser(id, user.Name, user.Email); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	ctx.JSON(http.StatusOK, gin.H{"message": "User updated"})
}

func (c *UserController) DeleteUser(ctx *gin.Context) {
	id, _ := strconv.Atoi(ctx.Param("id"))
	if err := c.userService.DeleteUser(id); err != nil {
		ctx.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	ctx.JSON(http.StatusOK, gin.H{"message": "User deleted"})
}
