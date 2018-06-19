package com.neituiquan.httpEvent;

import com.neituiquan.net.RequestEventModel;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class UpdateResumeEventModel extends RequestEventModel {

    public UpdateResumeEventModel(int eventId) {
        this.eventId = eventId;
    }
}
