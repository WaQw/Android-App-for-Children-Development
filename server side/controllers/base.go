package controllers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"growingTreeApiServer/models"
)

const (
	FailCode  = -1
	RECODE_OK = 200
)

type BaseController struct {
	beego.Controller
}

func (c *BaseController) RetData(data interface{}) {
	c.Data["json"] = data
	c.ServeJSON()
}

func (c *BaseController) SendJsonResponse(msg string, data interface{}) {
	c.ServeJsonResponse(RECODE_OK, msg, data)
}
func (c *BaseController) SendErrJsonResponse(errMsg string, data interface{}) {
	c.ServeJsonResponse(FailCode, errMsg, data)
}

func (c *BaseController) ServeJsonResponse(code int, msg string, data interface{}) {
	var r = ResultWrapper{}
	r.Code = code
	r.Msg = msg
	r.Data = data

	c.Data["json"] = r
	c.ServeJSON()
}

func (c *BaseController) GetLoginUser() *models.User {
	userName := c.Ctx.Request.Header.Get("username")
	logs.Info("userName", userName)
	user, err := models.GetUserByName(userName)
	if err != nil {
		return nil
	}
	logs.Info("userName", user)

	return user
}

func (c *BaseController) CheckLogin(username, password string) bool {
	return true
}
