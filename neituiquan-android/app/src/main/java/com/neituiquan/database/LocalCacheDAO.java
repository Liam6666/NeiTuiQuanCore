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
    List<ChatGroupEntity> getChatGroupList();

    /**
     * 根据聊天组id获取历史信息
     * @param groupId
     * @return
     */
    List<ChatEntity> getChatHistory(String groupId);

    /**
     * 获取一个消息组里未读的消息数量
     * @param groupId
     * @return
     */
    int getGroupNotReadCount(String groupId);

    /**
     * 获取一个消息组里最近的一条消息
     * @param groupId
     * @return
     */
    ChatEntity getGroupLastChat(String groupId);

    boolean hasId(String id);

    boolean hasGroup(String groupId);
}
