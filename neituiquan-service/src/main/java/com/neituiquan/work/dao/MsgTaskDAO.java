package com.neituiquan.work.dao;

import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.MsgTaskEntity;

import java.util.List;

public interface MsgTaskDAO {

    boolean addMsgTask(MsgTaskEntity entity);


    MsgTaskEntity findMsgTaskById(String id);


    List<MsgTaskEntity> getMsgTaskList(String receiveId);


    boolean delMsgTaskById(String id);


    boolean delMsgTaskAdd2History(String id,ChatHistoryEntity entity);
}
