package models

import (
	"errors"
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
)

var (
	UserList map[string]*User
)

func init() {
	RegisterModel(new(User))
	UserList = make(map[string]*User)
	u := User{11, "astaxie", "11111", "130", 1,
		20, "Singapore", "astaxie@gmail.com"}
	UserList["user_11111"] = &u
}

type User struct {
	Id       int64
	UserName string
	Password string
	NickName string
	Gender   int
	Age      int
	Address  string
	Email    string
}

func AddUser(u *User) (id int64, err error) {
	//u.Id = "user_" + strconv.FormatInt(time.Now().UnixNano(), 10)
	//UserList[u.Id] = &u

	o := orm.NewOrm()

	id, err = o.Insert(u)
	if err != nil {
		logs.Error(err)
	}
	logs.Info(id)
	return id, err
}
func GetUserByName(userName string) (*User, error) {
	o := orm.NewOrm()
	user := &User{UserName: userName}
	err := o.QueryTable(user).Filter("user_name", userName).One(user)
	if err != nil {
		return nil, errors.New("User not exists")
	}

	return user, nil
}
func GetUser(uid int64) (*User, error) {
	o := orm.NewOrm()

	user := User{}
	err := o.QueryTable(user).Filter("id", uid).One(&user)
	//err := o.Read(user)
	if err != nil {
		return nil, errors.New("User not exists")
	}
	return &user, nil
}

func GetAllUsers() []User {
	o := orm.NewOrm()

	var users []User
	i, e := o.QueryTable("user").All(&users)
	if e != nil {
		logs.Trace(e)
	}
	logs.Info(i)
	return users
}

func UpdateUser(uid string, uu *User) (a *User, err error) {
	if u, ok := UserList[uid]; ok {
		if uu.UserName != "" {
			u.UserName = uu.UserName
		}
		if uu.Password != "" {
			u.Password = uu.Password
		}
		if uu.Age != 0 {
			u.Age = uu.Age
		}
		if uu.Address != "" {
			u.Address = uu.Address
		}
		if uu.Gender != -1 {
			u.Gender = uu.Gender
		}
		if uu.Email != "" {
			u.Email = uu.Email
		}
		return u, nil
	}
	return nil, errors.New("User Not Exist")
}

func Login(username, password string) (*User, error) {
	o := orm.NewOrm()

	user := User{}
	err := o.QueryTable(user).Filter("user_name", username).Filter("password", password).One(&user)

	if err != nil {
		return nil, err
	}
	return &user, nil
}

func Register(username, password string) bool {
	return false
}

func DeleteUser(uid string) {
	delete(UserList, uid)
}
