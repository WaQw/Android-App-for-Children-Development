package controllers

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"github.com/satori/go.uuid"
	"path"
	"strings"
)

type StorageController struct {
	BaseController
}

// @Title CreateUser
// @router /upload [post]
func (this *StorageController) Create() {
	wrapper := GenerateResultWrapper()
	defer this.RetData(wrapper)

	//file，这是一个key值，对应的是html中input type-‘file’的name属性值
	f, h, err := this.GetFile("file")
	if err != nil {
		wrapper.Msg = "上传文件失败"
		return
	}

	ext := path.Ext(h.Filename)

	var AllowExtMap = map[string]bool{
		".jpg":  true,
		".jpeg": true,
		".png":  true,
	}
	if ok := AllowExtMap[ext]; !ok {
		wrapper.Msg = "后缀不符合上传要求"
		return
	}

	logs.Info("ext:", ext)
	//得到文件的名称
	fileName := h.Filename
	arr := strings.Split(fileName, ":")
	if len(arr) > 1 {
		index := len(arr) - 1
		fileName = arr[index]
	}
	logs.Info("文件名称:", fileName)

	//关闭上传的文件，不然的话会出现临时文件不能清除的情况
	err = f.Close()
	if err != nil {
		logs.Error(err)
		wrapper.Msg = "上传失败"
		return
	}

	filePath := "upload/"

	key := generateKey(fileName)

	logs.Info("path join:", path.Join(filePath, key))

	//保存文件到指定的位置
	//static/uploadfile,这个是文件的地址，第一个static前面不要有/
	err = this.SaveToFile("file", path.Join(filePath, key))
	if err != nil {
		logs.Error(err)
		wrapper.Msg = "上传失败"
		return
	}

	imgHost := beego.AppConfig.String("img_host")
	url := path.Join(imgHost, key)

	//wrapper.Data = map[string]interface{}{"url": url}
	wrapper.Data = url
	wrapper.Code = RECODE_OK
	wrapper.Msg = "上传文件成功"

}

func generateKey(name string) string {
	u, _ := uuid.NewV4()
	return u.String() + path.Ext(name)
}
