package models

import (
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
	"time"
)

func init() {

}

type Milestone struct {
	Id          int64
	Title       string
	Des         string
	Assessment  string
	PhotoUrl    string
	CreatedTime time.Time
}

func AddMilestone(m *Milestone) (id int64, err error) {
	o := orm.NewOrm()

	id, err = o.Insert(m)
	//i, e := o.InsertOrUpdate(u, "user_name", "password", "phone", "email", "sex", "age")
	if err != nil {
		logs.Error(err)
	}
	logs.Info(id)
	return id, err
}
