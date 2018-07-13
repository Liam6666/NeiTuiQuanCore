package com.neituiquan.httpEvent;

import com.neituiquan.database.ChatDBEntity;
import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatEventModel extends RequestEventModel {

    public ChatDBEntity chatDBEntity;

    public ChatEventModel(ChatDBEntity chatDBEntity){
        this.chatDBEntity = chatDBEntity;
    }
}
