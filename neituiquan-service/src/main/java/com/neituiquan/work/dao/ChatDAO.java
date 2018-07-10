package com.neituiquan.work.dao;

import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.ChatLoopEntity;

import java.util.List;

public interface ChatDAO {

    /**
     * 添加一条消息
     * @param entity
     * @return
     */
    boolean addMsg(ChatHistoryEntity entity);

    /**
     * 获取消息
     * @param receiveId
     * @return
     */
    List<ChatLoopEntity> findMsgByReceiveId(String receiveId);

    /**
     * 收到消息，更改状态
     * @param id
     * @return
     */
    boolean receiveMsg(String id);
}
