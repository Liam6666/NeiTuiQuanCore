package com.neituiquan.httpEvent;

import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/7/11.
 * <p>
 * email:nice_ohoh@163.com
 */

public class GetJobInfoEventModel extends RequestEventModel {

    public int eventId;

    public GetJobInfoEventModel(int eventId){
        this.eventId = eventId;
    }
}
