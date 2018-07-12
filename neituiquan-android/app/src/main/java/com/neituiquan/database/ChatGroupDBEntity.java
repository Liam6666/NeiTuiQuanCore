package com.neituiquan.database;

import java.io.Serializable;

/**
 * Created by Augustine on 2018/7/12.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatGroupDBEntity implements Serializable {

    private String otherSideId;//对方（接受者）id

    private String account;//当前登录账号

    private String osHeadImg;//对方头像

    private String osNickName;

    private String notReadCount;//未读条数

    private String lastChatId;//最后一条消息id

    private ChatDBEntity lastChatEntity;

    public ChatDBEntity getLastChatEntity() {
        return lastChatEntity;
    }

    public void setLastChatEntity(ChatDBEntity lastChatEntity) {
        this.lastChatEntity = lastChatEntity;
    }

    public String getOtherSideId() {
        return otherSideId;
    }

    public void setOtherSideId(String otherSideId) {
        this.otherSideId = otherSideId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOsHeadImg() {
        return osHeadImg;
    }

    public void setOsHeadImg(String osHeadImg) {
        this.osHeadImg = osHeadImg;
    }

    public String getOsNickName() {
        return osNickName;
    }

    public void setOsNickName(String osNickName) {
        this.osNickName = osNickName;
    }

    public String getNotReadCount() {
        return notReadCount;
    }

    public void setNotReadCount(String notReadCount) {
        this.notReadCount = notReadCount;
    }

    public String getLastChatId() {
        return lastChatId;
    }

    public void setLastChatId(String lastChatId) {
        this.lastChatId = lastChatId;
    }
}
