package com.neituiquan.entity;

import java.io.Serializable;

/**
 * Created by Augustine on 2018/7/12.
 * <p>
 * email:nice_ohoh@163.com
 *
 *
 * CREATE TABLE t_chat_group (
 formId PRIMARY KEY,
 account,
 fromNickName,
 fromHeadImg,
 notReadCount,
 lastChat,
 lastChatTime,
 lastFromNickName
 );

 * 消息列表
 */

public class MessageEntity extends AbsEntity implements Serializable {

    private String fromId;

    private String account;

    private String fromNickName;

    private String fromHeadImg;

    private String notReadCount;

    private String lastChat;

    private String lastChatTime;

    private String lastFromNickName;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getNotReadCount() {
        return notReadCount;
    }

    public void setNotReadCount(String notReadCount) {
        this.notReadCount = notReadCount;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public String getLastFromNickName() {
        return lastFromNickName;
    }

    public void setLastFromNickName(String lastFromNickName) {
        this.lastFromNickName = lastFromNickName;
    }
}

