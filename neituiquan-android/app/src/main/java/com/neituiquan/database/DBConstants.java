package com.neituiquan.database;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class DBConstants {

    public static final int VERSION = 1;

    public static final String NAME = "neituiquan.db";

    public static final String YES = "0";

    public static final String NO = "-1";

    /**
     * 聊天记录
     *
     * 1.消息列表
     *      1.头像
     *      2.昵称
     *      3.消息条数
     *      4.最近一条消息
     *      5.最近一条消息的时间
     *      6.未读数量
     * 2.聊天详情
     *      1.聊天记录
     *
     */
    public static final String TABLE_NAME = "t_chat_history";

    public static final String ID = "id";

    public static final String FORM_ID = "fromId";//聊天组、聊天对象id

    public static final String HEAD_IMG = "headImg";

    public static final String NICK_NAME = "nickName";

    public static final String CREATE_TIME = "createTime";

    public static final String MSG_DETAILS = "msgDetails";

    public static final String IS_READ = "isRead";

    public static final String IS_SELF = "isSelf";//是否是自己发送的消息


}
