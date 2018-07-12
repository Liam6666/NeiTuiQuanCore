package com.neituiquan.work.daoImpl;

import com.neituiquan.work.FinalData;
import com.neituiquan.work.dao.ChatDAO;
import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.ChatLoopEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ChatDaoImpl implements ChatDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addMsg(ChatHistoryEntity entity) {
        String sql = "insert into t_chat_history values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getGroupId(),entity.getFromId(),
                entity.getFromNickName(),entity.getFromHeadImg(),
                entity.getReceiveId(),entity.getReceiveNickName(),
                entity.getReceiveHeadImg(),entity.getMsgDetails(),
                entity.getMsgType(),entity.getAccount(),entity.getIsFrom(),
                entity.getIsRead(),entity.getCreateTime(),entity.getIsReceive()
        };
        jdbcTemplate.update(sql,params);
        sql = "insert into t_chat_loop values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        params = new String[]{
                entity.getId(),entity.getGroupId(),entity.getFromId(),
                entity.getFromNickName(),entity.getFromHeadImg(),
                entity.getReceiveId(),entity.getReceiveNickName(),
                entity.getReceiveHeadImg(),entity.getMsgDetails(),
                entity.getMsgType(),entity.getAccount(),entity.getIsFrom(),
                entity.getCreateTime()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public List<ChatLoopEntity> findMsgByReceiveId(String receiveId) {
        List<ChatLoopEntity> list = new ArrayList<>();
        String sql = "select * from t_chat_loop where receiveId = ?";
        jdbcTemplate.query(sql, new String[]{receiveId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                ChatLoopEntity entity = new ChatLoopEntity();
                setChatLoopValues(entity,resultSet);
                list.add(entity);
            }
        });
        return list;
    }

    @Override
    public boolean receiveMsg(String id) {
        String sql = "delete from t_chat_loop where id = ?";
        jdbcTemplate.update(sql,new String[]{id});
        sql = "update t_chat_history set isReceive = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.YES,id});
        return true;
    }


    private void setChatLoopValues(ChatLoopEntity entity, ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setGroupId(resultSet.getString("groupId"));
        entity.setFromId(resultSet.getString("fromId"));
        entity.setFromNickName(resultSet.getString("fromNickName"));
        entity.setFromHeadImg(resultSet.getString("fromHeadImg"));
        entity.setReceiveId(resultSet.getString("receiveId"));
        entity.setReceiveNickName(resultSet.getString("receiveNickName"));
        entity.setReceiveHeadImg(resultSet.getString("receiveHeadImg"));
        entity.setMsgDetails(resultSet.getString("msgDetails"));
        entity.setMsgType(resultSet.getString("msgType"));
        entity.setAccount(resultSet.getString("account"));
        entity.setIsFrom(resultSet.getString("isFrom"));
        entity.setCreateTime(resultSet.getString("createTime"));
    }
}
