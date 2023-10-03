package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
	"reflect"
)

var (
	tablePrefix = beego.AppConfig.String("table_prefix")
	o           orm.Ormer
)

func init() {
	logs.Info("models init")
	logs.Info("table prefix:", tablePrefix)
}

type M struct {
}

func (this *M) Object() orm.Ormer {
	return orm.NewOrm()
}

func RegisterModel(models ...interface{}) {
	//logs.Info("register model:", models...)
	for _, model := range models {
		val := reflect.ValueOf(model)
		typ := reflect.Indirect(val).Type()

		logs.Info(typ, "init")
	}
	orm.RegisterModelWithPrefix(tablePrefix, models...)
}

func db() orm.Ormer {
	if o == nil {
		logs.Info("orm is nil, initial")
		o = orm.NewOrm()
	}
	return o
}
