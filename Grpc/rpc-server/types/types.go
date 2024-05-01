package types

type LoginReq struct {
	Name string `json:"name" binding:"required"`
}
