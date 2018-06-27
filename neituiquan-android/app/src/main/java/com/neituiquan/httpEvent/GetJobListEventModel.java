package com.neituiquan.httpEvent;

import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class GetJobListEventModel extends RequestEventModel {

    public GetJobListEventModel(int eventId) {
        this.eventId = eventId;
    }
}
