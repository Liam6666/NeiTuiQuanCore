package com.neituiquan.database;

import java.io.Serializable;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 聊天内容
 *
 * "(id,groupId,fromId,fromHeadImg,fromNickName,createTime,msgDetails,isRead)";
 */

public class ChatEntity implements Serializable {

    private String id;

    private String groupId;

    private String fromId;

    private String fromHeadImg;

    private String fromNickName;

    private String createTime;

    private String msgDetails;

    private String isRead;

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

    public String getFromHeadImg() {
        return fromHeadImg;
    }

    public void setFromHeadImg(String fromHeadImg) {
        this.fromHeadImg = fromHeadImg;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgDetails() {
        return msgDetails;
    }

    public void setMsgDetails(String msgDetails) {
        this.msgDetails = msgDetails;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
