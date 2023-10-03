package com.viadroid.app.growingtree.entries;

public class BabyRecord {
    private int id;
    private String babyId;
    private String height;
    private String weight;
    private String headCircumference;
    private String bmi;
    private String photoUrl;
    private String addTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeadCircumference() {
        return headCircumference;
    }

    public void setHeadCircumference(String headCircumference) {
        this.headCircumference = headCircumference;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "BabyRecord{" +
                "id=" + id +
                ", babyId='" + babyId + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", headCircumference='" + headCircumference + '\'' +
                ", bmi='" + bmi + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", addTime='" + addTime + '\'' +
                '}';
    }
}
