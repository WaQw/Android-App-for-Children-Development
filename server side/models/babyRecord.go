package models

import (
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
)

func init() {
	RegisterModel(new(BabyRecord))
}

type BabyRecord struct {
	Id                int64  `json:"id"`
	BabyId            int64  `json:"babyId"`
	Height            string `json:"height"`
	Weight            string `json:"weight"`
	HeadCircumference string `json:"headCircumference"`
	Bmi               string `json:"bmi"`
	PhotoUrl          string `json:"photoUrl"`
}

func AddBabyRecord(br *BabyRecord) (id int64, err error) {
	//u.Id = "user_" + strconv.FormatInt(time.Now().UnixNano(), 10)
	//UserList[u.Id] = &u

	o := orm.NewOrm()

	id, err = o.Insert(br)
	if err != nil {
		logs.Error(err)
	}
	logs.Info(id)
	return id, err
}

func GetBabyRecord(bid int64) (*BabyRecord, error) {
	o := orm.NewOrm()
	babyRecord := &BabyRecord{}
	err := o.QueryTable(babyRecord).Filter("baby_id", bid).One(babyRecord)
	if err != nil {
		return nil, err
	}
	return babyRecord, nil

}
