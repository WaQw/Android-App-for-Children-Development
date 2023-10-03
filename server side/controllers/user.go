package controllers

import (
	"encoding/json"
	"github.com/astaxie/beego/logs"
	"growingTreeApiServer/models"
)

// Operations about Users
type UserController struct {
	BaseController
}

// @Title CreateUser
// @Description create users
// @Param	body		body 	models.User	true		"body for user content"
// @Success 200 {int} models.User.Id
// @Failure 403 body is empty
// @router /register [post]
func (u *UserController) Register() {
	userName := u.GetString("username")
	password := u.GetString("password")

	uid, err := models.AddUser(&models.User{UserName: userName, Password: password})
	if err != nil {
		u.SendErrJsonResponse("注册用户出错", err)
	}

	rp := make(map[string]interface{})
	rp["uid"] = uid
	rp["token"] = ""
	u.SendJsonResponse("注册用户成功", rp)

	//u.Data["json"] = map[string]interface{}{"uid": uid}
	//u.ServeJSON()
}

// @Title GetAll
// @Description get all Users
// @Success 200 {object} models.User
// @router / [get]
func (u *UserController) GetAll() {
	users := models.GetAllUsers()
	u.Data["json"] = users
	u.ServeJSON()
}

// @Title Get
// @Description get user by uid
// @Param	uid		path 	string	true		"The key for staticblock"
// @Success 200 {object} models.User
// @Failure 403 :uid is empty
// @router /:uid [get]
func (c *UserController) Get() {
	uid, _ := c.GetInt64(":uid", 0)

	logs.Info("uid:", uid)
	if uid != 0 {
		user, err := models.GetUser(uid)
		if err != nil {
			c.Data["json"] = err.Error()
		} else {
			c.Data["json"] = user
		}
	}
	c.ServeJSON()
}

// @Title Update
// @Description update the user
// @Param	uid		path 	string	true		"The uid you want to update"
// @Param	body		body 	models.User	true		"body for user content"
// @Success 200 {object} models.User
// @Failure 403 :uid is not int
// @router /:uid [put]
func (u *UserController) Put() {
	uid := u.GetString(":uid")
	if uid != "" {
		var user models.User
		json.Unmarshal(u.Ctx.Input.RequestBody, &user)
		uu, err := models.UpdateUser(uid, &user)
		if err != nil {
			u.Data["json"] = err.Error()
		} else {
			u.Data["json"] = uu
		}
	}
	u.ServeJSON()
}

// @Title Delete
// @Description delete the user
// @Param	uid		path 	string	true		"The uid you want to delete"
// @Success 200 {string} delete success!
// @Failure 403 uid is empty
// @router /:uid [delete]
func (u *UserController) Delete() {
	uid := u.GetString(":uid")
	models.DeleteUser(uid)
	u.Data["json"] = "delete success!"
	u.ServeJSON()
}

// @Title Login
// @Description Logs user into the system
// @Param	username		query 	string	true		"The username for login"
// @Param	password		query 	string	true		"The password for login"
// @Success 200 {string} login success
// @Failure 403 user not exist
// @router /login [get]
func (u *UserController) Login() {
	username := u.GetString("username")
	password := u.GetString("password")

	logs.Info("user:", username, "password:", password)

	if user, err := models.Login(username, password); err == nil {
		babyNum := models.CountBabyByUid(user.Id)

		rp := make(map[string]interface{})
		rp["userName"] = user.UserName
		rp["token"] = ""
		rp["babyNum"] = babyNum

		u.SendJsonResponse("登录成功", rp)
	} else {
		u.SendErrJsonResponse("用户不存在", err)
	}
	u.ServeJSON()
}

// @Title logout
// @Description Logs out current logged in user session
// @Success 200 {string} logout success
// @router /logout [get]
func (u *UserController) Logout() {
	u.Data["json"] = "logout success"
	u.ServeJSON()
}
