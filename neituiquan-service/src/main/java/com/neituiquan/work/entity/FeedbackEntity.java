package com.neituiquan.work.entity;

import java.io.Serializable;

/**
 * 反馈
 */
public class FeedbackEntity implements Serializable {

    private  String id;


    private  String userId;

    //反馈类型
    private  String feedbackType;


    private  String message;


    private  String createTime;

    //是否已解决、已处理
    private  String isSolve;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsSolve() {
        return isSolve;
    }

    public void setIsSolve(String isSolve) {
        this.isSolve = isSolve;
    }
}
