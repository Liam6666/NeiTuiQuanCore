package com.neituiquan.net;

/**
 * Created by Augustine on 2018/6/15.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SuccessEventModel {

    public String response;

    public int taskId;

    public SuccessEventModel(){}

    public SuccessEventModel(String response) {
        this.response = response;
    }

    public SuccessEventModel(String response, int taskId) {
        this.response = response;
        this.taskId = taskId;
    }

    public SuccessEventModel(int taskId) {
        this.taskId = taskId;
    }
}
