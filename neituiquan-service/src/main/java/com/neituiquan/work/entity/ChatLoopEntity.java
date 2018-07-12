package com.neituiquan.work.entity;

import java.io.Serializable;

public class ChatLoopEntity implements Serializable {

    private  String id;


    private  String groupId;


    private  String fromId;


    private  String fromNickName;


    private  String fromHeadImg;


    private  String receiveId;


    private  String receiveNickName;


    private  String receiveHeadImg;


    private  String msgDetails;


    private  String msgType;


    private  String account;


    private  String isFrom;


    private  String createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getFromHeadImg() {
        return fromHeadImg;
    }

    public void setFromHeadImg(String fromHeadImg) {
        this.fromHeadImg = fromHeadImg;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getReceiveNickName() {
        return receiveNickName;
    }

    public void setReceiveNickName(String receiveNickName) {
        this.receiveNickName = receiveNickName;
    }

    public String getReceiveHeadImg() {
        return receiveHeadImg;
    }

    public void setReceiveHeadImg(String receiveHeadImg) {
        this.receiveHeadImg = receiveHeadImg;
    }

    public String getMsgDetails() {
        return msgDetails;
    }

    public void setMsgDetails(String msgDetails) {
        this.msgDetails = msgDetails;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
