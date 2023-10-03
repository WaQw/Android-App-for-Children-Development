package controllers

import (
	"fmt"
	"growingTreeApiServer/models"
	"strconv"
)

type BabyController struct {
	BaseController
}

// @Title CreateBaby
// @Description create baby
// @Param	body		body 	models.Baby	true
// @Success 200 {int} models.Baby.Id
// @Failure 403 body is empty
// @router /add [post]
func (c *BabyController) CreateBaby() {
	nickName := c.GetString("nickName")
	gender, _ := c.GetInt("gender", 0)
	birthday := c.GetString("birthday")
	headImgUrl := c.GetString("headImgUrl")

	height := c.GetString("height")
	weight := c.GetString("weight")
	headCircumference := c.GetString("headCircumference")

	w, _ := strconv.ParseFloat(weight, 8)
	h, _ := strconv.ParseFloat(height, 8)
	bmi := w / h //体质指数(BMI)=体重（千克）/身高（米）

	user := c.GetLoginUser()
	if user == nil {
		c.SendErrJsonResponse("添加宝宝出错", nil)
		return
	}
	baby := &models.Baby{
		Uid:        user.Id,
		NickName:   nickName,
		Gender:     gender,
		Birthday:   birthday,
		HeadImgUrl: headImgUrl,
	}

	bid, err := models.AddBaby(baby)
	if err != nil {
		c.SendErrJsonResponse("添加宝宝出错", err)
		return
	}

	babyRecord := &models.BabyRecord{
		BabyId:            bid,
		Height:            height,
		Weight:            weight,
		HeadCircumference: headCircumference,
		Bmi:               fmt.Sprintf("%.2f", bmi),
		PhotoUrl:          headImgUrl,
	}

	rId, err := models.AddBabyRecord(babyRecord)
	if err != nil {
		c.SendErrJsonResponse("添加宝宝出错", err)
		return
	}

	rp := make(map[string]interface{})
	rp["bid"] = bid
	rp["rid"] = rId
	c.SendJsonResponse("添加宝宝成功", rp)

}

// @Title GetBaby
// @router / [get]
func (c *BabyController) Get() {
	//bid, _ := c.GetInt64(":bid", 0)
	user := c.GetLoginUser()
	if user == nil {
		c.SendErrJsonResponse("获取宝宝信息出错", nil)
		return
	}

	baby, err := models.GetFirstBabyByUid(user.Id)

	if err != nil {
		c.SendErrJsonResponse("获取宝宝信息出错", err)
		return
	}

	record, err := models.GetBabyRecord(baby.Id)
	if err != nil {
		c.SendErrJsonResponse("获取宝宝信息出错", err)
		return
	}

	rp := make(map[string]interface{})
	rp["id"] = baby.Id
	rp["uid"] = baby.Uid
	rp["nickName"] = baby.NickName
	rp["gender"] = baby.Gender
	rp["birthday"] = baby.Birthday
	rp["headImgUrl"] = baby.HeadImgUrl
	rp["babyRecord"] = record
	c.SendJsonResponse("获取宝宝信息成功", rp)
}
