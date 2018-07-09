package com.neituiquan.work.service;

import com.neituiquan.work.FinalData;
import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.daoImpl.MsgTaskDAOImpl;
import com.neituiquan.work.daoImpl.UserDAOImpl;
import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.MsgTaskEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgTaskService {

    @Autowired
    private MsgTaskDAOImpl taskDAO;

    @Autowired
    private UserDAOImpl userDAO;

    public AbsEntity addMsgTask(MsgTaskEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        entity.setIsRead(FinalData.NO);
        entity.setIsReceive(FinalData.NO);
        taskDAO.addMsgTask(entity);
        return new AbsEntity();
    }


    public AbsEntity findMsgTaskById(String id){
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = taskDAO.findMsgTaskById(id);
        if(absEntity.data == null){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "查询失败，没有找到";
        }
        return absEntity;
    }


    public AbsEntity getMsgTaskList(String receiveId){
        List<MsgTaskEntity> list = taskDAO.getMsgTaskList(receiveId);
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }


    public AbsEntity delMsgTaskById(String id){
        taskDAO.delMsgTaskById(id);
        return new AbsEntity();
    }


    public AbsEntity delMsgTaskAdd2History(String id){
        ChatHistoryEntity entity = new ChatHistoryEntity();
        entity.setId(id);
        MsgTaskEntity msgTaskEntity = taskDAO.findMsgTaskById(id);
        if(msgTaskEntity != null){
            entity.setFormId(msgTaskEntity.getFromId());
            entity.setReceiveId(msgTaskEntity.getReceiveId());
            entity.setFromName(userDAO.findUserById(msgTaskEntity.getFromId()).getNickName());
            entity.setReceiveName(userDAO.findUserById(msgTaskEntity.getReceiveId()).getNickName());
            entity.setCreateTime(msgTaskEntity.getCreateTime());
            entity.setMsgDetails(msgTaskEntity.getMsgDetails());
            entity.setMsgType(msgTaskEntity.getMsgType());
            entity.setIsRead(FinalData.NO);
            entity.setIsReceive(FinalData.YES);
        }
        taskDAO.delMsgTaskAdd2History(id,entity);
        return new AbsEntity();
    }
}
