package com.neituiquan.httpEvent;

import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/7/5.
 * <p>
 * email:nice_ohoh@163.com
 */

public class GetCodeEventModel extends RequestEventModel {

    public GetCodeEventModel(int eventId){
        this.eventId = eventId;
    }
}
