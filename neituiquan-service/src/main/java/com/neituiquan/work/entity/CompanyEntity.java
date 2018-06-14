package com.neituiquan.work.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户绑定的公司信息
 */

public class CompanyEntity implements Serializable{

    private  String id;

    private  String userId;

    //公司名
    private  String companyName;

    //省
    private  String province;

    //市
    private  String city;

    //详细位置
    private  String address;

    //企业介绍
    private  String introduce;

    //创立时间
    private  String creationTime;

    //企业人数
    private  String peopleNum;

    //公司主页
    private  String linkUrl;

    //标签
    private  String labels;

    //删除标记
    private  String isDel;

    private List<CompanyImgEntity> imgList = new ArrayList<>();

    //评分
    private int score;

    private int scoreCount;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getPeopleNum() {
        return peopleNum;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getLabels() {
        return labels;
    }

    public int getScore() {
        return score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public List<CompanyImgEntity> getImgList() {
        return imgList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setPeopleNum(String peopleNum) {
        this.peopleNum = peopleNum;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setImgList(List<CompanyImgEntity> imgList) {
        this.imgList = imgList;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
