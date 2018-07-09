package com.neituiquan.database;

import java.io.Serializable;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 聊天组
 *
 * id,groupId,headImg,groupName,updateTime
 *
 */

public class ChatGroupEntity implements Serializable {

    private String groupId;

    private String headImg;

    private String groupName;

    private String updateTime;

    private String notReadCount;

    private String lastChat;

    private String lastChatTime;

    private String lastFromNickName;

    public String getLastFromNickName() {
        return lastFromNickName;
    }

    public void setLastFromNickName(String lastFromNickName) {
        this.lastFromNickName = lastFromNickName;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
