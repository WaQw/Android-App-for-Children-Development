package db

import (
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
)

var (
	db          orm.Ormer
	tablePrefix = beego.AppConfig.String("table_prefix")
)

func init() {
	logs.Info("initial init")
	//orm.Debug = true

	dbDriver := beego.AppConfig.String("db_driver")
	dbHost := beego.AppConfig.String("db_host")
	dbPort := beego.AppConfig.String("db_port")
	dbUser := beego.AppConfig.String("db_user")
	dbPass := beego.AppConfig.String("db_pass")
	dbName := beego.AppConfig.String("db_name")

	logs.Info(dbUser, dbPass, dbHost, dbPort, dbName)

	dataSource := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8", dbUser, dbPass, dbHost, dbPort, dbName)

	_ = orm.RegisterDriver(dbDriver, orm.DRMySQL)
	_ = orm.RegisterDataBase("default", dbDriver, dataSource, 30)

	orm.SetMaxOpenConns("default", 30)

	//自动建表配置
	_ = orm.RunSyncdb("default", false, true)

	db = orm.NewOrm()

	//initData()
}

func initData() {

}
