package com.neituiquan.httpcode;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Augustine on 2018/7/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public abstract class BaseHandler extends Handler {

    private int requestId;

    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
        switch (msg.what){
            case HttpURLCode.WAITING_CODE:
                requesting();
                break;
            case HttpURLCode.COMPLETE_CODE:
                complete();
                break;
            case HttpURLCode.SUCCESS_CODE:
                success(msg,requestId);
                break;
            case HttpURLCode.ERROR_CODE:
                error(msg,requestId);
                break;
        }
    }

    public void bindId(int requestId){
        this.requestId = requestId;
    }

    public abstract void requesting();

    public abstract void complete();

    public abstract void success(Message msg,int requestId);

    public abstract void error(Message msg,int requestId);
}
