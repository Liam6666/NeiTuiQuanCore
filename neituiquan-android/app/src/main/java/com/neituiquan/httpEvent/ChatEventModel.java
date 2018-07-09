package com.neituiquan.httpEvent;

import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatEventModel extends RequestEventModel {

    public String groupId;

    public ChatEventModel(String groupId){
        this.groupId = groupId;
    }
}
