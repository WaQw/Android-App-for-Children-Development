package com.viadroid.app.growingtree.entries;

/**
 * 里程碑
 */
public class Milestone {
    /**
     * 1.时间
     * 2.标题（第一次xxx）
     * 3.照片
     * 4.文字描述
     * 5.app评估结果
     */
    private String createdTime;
    private String title;
    private String photoUrl;
    private String des;
    private String assessment;
    private int id;
    private int babyId;
    private int type;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAssessment() {
        return assessment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAssessment(String assessment) {

        this.assessment = assessment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBabyId() {
        return babyId;
    }

    public void setBabyId(int babyId) {
        this.babyId = babyId;
    }

    @Override
    public String toString() {
        return "Milestone{" +
                "createdTime='" + createdTime + '\'' +
                ", title='" + title + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", des='" + des + '\'' +
                ", assessment='" + assessment + '\'' +
                ", id=" + id +
                ", babyId=" + babyId +
                ", type=" + type +
                '}';
    }
}
