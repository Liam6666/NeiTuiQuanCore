package com.neituiquan.work.entity;

import java.io.Serializable;

/**
 * 广告轮播
 */
public class BannerEntity implements Serializable {

    private  String id;


    private  String imgUrl;


    private  String titleMsg;


    private  String sort;


    private  String linkUrl;


    private  String isDel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitleMsg() {
        return titleMsg;
    }

    public void setTitleMsg(String titleMsg) {
        this.titleMsg = titleMsg;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }
}
