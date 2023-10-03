package db

import (
	"github.com/astaxie/beego/orm"
	"reflect"
	"strings"
)

func DB() orm.Ormer {
	return db
}

func Raw(query string, args ...interface{}) orm.RawSeter {
	return db.Raw(query, args...)
}

func Read(md interface{}, cols ...string) error {
	return db.Read(md, cols...)
}

func Insert(md interface{}) (int64, error) {
	return db.Insert(md)
}

func Update(md interface{}, cols ...string) (int64, error) {
	return db.Update(md, cols...)
}

func Delete(md interface{}, cols ...string) (int64, error) {
	return db.Delete(md, cols...)
}

func QueryTable(model interface{}) orm.QuerySeter {
	return db.QueryTable(realTableName(model))
}

func LoadRelated(md interface{}, name string, args ...interface{}) (int64, error) {
	return db.LoadRelated(md, name, args)
}

func realTableName(model interface{}) string {
	val := reflect.ValueOf(model)
	fullTableName := tablePrefix + getTableName(val)
	return fullTableName
}

func getTableName(val reflect.Value) string {
	if fun := val.MethodByName("TableName"); fun.IsValid() {
		vals := fun.Call([]reflect.Value{})
		// has return and the first val is string
		if len(vals) > 0 && vals[0].Kind() == reflect.String {
			return vals[0].String()
		}
	}
	return snakeString(reflect.Indirect(val).Type().Name())
}

// snake string, XxYy to xx_yy , XxYY to xx_y_y
func snakeString(s string) string {
	data := make([]byte, 0, len(s)*2)
	j := false
	num := len(s)
	for i := 0; i < num; i++ {
		d := s[i]
		if i > 0 && d >= 'A' && d <= 'Z' && j {
			data = append(data, '_')
		}
		if d != '_' {
			j = true
		}
		data = append(data, d)
	}
	return strings.ToLower(string(data[:]))
}
