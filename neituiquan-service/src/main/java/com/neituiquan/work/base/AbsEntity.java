package com.neituiquan.work.base;

import com.neituiquan.work.FinalData;

import java.io.Serializable;

public class AbsEntity implements Serializable{


    public int code = FinalData.SUCCESS_CODE;

    public String msg = FinalData.SUCCESS;

    public int dataTotalCount = 0;

    public long time = System.currentTimeMillis();

    public Object data = null;


}
