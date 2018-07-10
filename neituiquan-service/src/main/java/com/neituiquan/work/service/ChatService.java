package com.neituiquan.work.service;

import com.neituiquan.work.FinalData;
import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.daoImpl.ChatDaoImpl;
import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.ChatLoopEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatDaoImpl dao;

    public AbsEntity addMsg(ChatHistoryEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        entity.setIsRead(FinalData.NO);
        entity.setIsReceive(FinalData.NO);
        if(StringUtils.isEmpty(entity.getGroupId())){
            entity.setIsGroup(FinalData.NO);
        }else{
            entity.setIsGroup(FinalData.YES);
        }
        dao.addMsg(entity);
        return new AbsEntity();
    }

    public AbsEntity findMsgByReceiveId(String receiveId){
        List<ChatLoopEntity> list = dao.findMsgByReceiveId(receiveId);
        AbsEntity absEntity = new AbsEntity();
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }

    public AbsEntity receiveMsg(String id){
        dao.receiveMsg(id);
        return new AbsEntity();
    }
}
