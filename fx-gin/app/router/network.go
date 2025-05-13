package router

import (
	"fx-server/app/controller"
	"github.com/gin-gonic/gin"
)

func SetupRouter(r *gin.Engine, userController *controller.UserController, authMiddleware gin.HandlerFunc) {
	v1 := r.Group("/api/v1")
	{
		users := v1.Group("/users")
		users.Use(authMiddleware)
		{
			users.GET("", userController.GetAllUsers)
			users.GET("/:id", userController.GetUser)
			users.POST("", userController.CreateUser)
			users.PUT("/:id", userController.UpdateUser)
			users.DELETE("/:id", userController.DeleteUser)
		}
	}
}
