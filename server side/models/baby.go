package models

import (
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
)

func init() {
	RegisterModel(new(Baby))
}

type Baby struct {
	Id         int64  `json:"id"`
	Uid        int64  `json:"uid"`
	NickName   string `json:"nickName"`
	Gender     int    `json:"gender"`
	Birthday   string `json:"birthday"`
	HeadImgUrl string `json:"headImgUrl"`

	//Age               int
	//Height            string
	//Weight            string
	//HeadCircumference string
	//BMI               string
}

func AddBaby(b *Baby) (id int64, err error) {
	//u.Id = "user_" + strconv.FormatInt(time.Now().UnixNano(), 10)
	//UserList[u.Id] = &u

	o := orm.NewOrm()

	id, err = o.Insert(b)
	if err != nil {
		logs.Error(err)
	}
	logs.Info(id)
	return id, err
}

func GetBaby(id int64) (*Baby, error) {
	o := orm.NewOrm()

	baby := Baby{}
	err := o.QueryTable(baby).Filter("id", id).One(&baby)

	if err != nil {
		return nil, err
	}
	return &baby, nil
}

func GetFirstBabyByUid(uid int64) (*Baby, error) {
	o := orm.NewOrm()

	baby := &Baby{}
	err := o.QueryTable(baby).Filter("uid", uid).One(baby)
	if err != nil {
		return nil, err
	}
	return baby, nil
}

func CountBabyByUid(uid int64) int64 {
	o := orm.NewOrm()

	count, err := o.QueryTable(Baby{}).Filter("uid", uid).Count()
	if err != nil {
		count = 0
	}
	return count
}
