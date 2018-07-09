package com.neituiquan.database;

import java.util.List;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public interface LocalCacheDAO {
    /**
     * 添加一条信息到本地
     * @param entity
     * @return
     */
    boolean add(ChatEntity entity);

    /**
     * 获取消息列表
     * @return
     */
    List<ChatEntity> getChatList();

    /**
     * 根据聊天组id获取历史信息
     * @param fromId
     * @return
     */
    List<ChatEntity> getChatHistory(String fromId);

}
