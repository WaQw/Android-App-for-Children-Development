package main

import (
	"github.com/astaxie/beego"
	_ "growingTreeApiServer/initial"
	_ "growingTreeApiServer/routers"
)

func main() {
	beego.SetStaticPath("/img", "upload/")
	if beego.BConfig.RunMode == "dev" {
		beego.BConfig.WebConfig.DirectoryIndex = true
		beego.BConfig.WebConfig.StaticDir["/swagger"] = "swagger"
	}
	beego.Run()
}
