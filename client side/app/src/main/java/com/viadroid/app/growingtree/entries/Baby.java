package com.viadroid.app.growingtree.entries;

public class Baby {

    private int id;
    private String uid;
    private String nickName;
    private int gender;
    private String birthday;
    private String headImgUrl;
    private BabyRecord babyRecord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public BabyRecord getBabyRecord() {
        return babyRecord;
    }

    public void setBabyRecord(BabyRecord babyRecord) {
        this.babyRecord = babyRecord;
    }

    @Override
    public String toString() {
        return "Baby{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", babyRecord=" + babyRecord +
                '}';
    }
}
