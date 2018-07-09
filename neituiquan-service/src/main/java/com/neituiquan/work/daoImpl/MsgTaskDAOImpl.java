package com.neituiquan.work.daoImpl;

import com.neituiquan.work.dao.MsgTaskDAO;
import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.entity.MsgTaskEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MsgTaskDAOImpl implements MsgTaskDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public boolean addMsgTask(MsgTaskEntity entity) {
        String sql = "insert into t_msg_task values (?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getFromId(),entity.getReceiveId(),
                entity.getMsgType(),entity.getMsgDetails(),entity.getCreateTime(),
                entity.getIsRead(),entity.getIsReceive()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public MsgTaskEntity findMsgTaskById(String id) {
        String sql = "select * from t_msg_task where id = ?";
        MsgTaskEntity entity = new MsgTaskEntity();
        jdbcTemplate.query(sql, new String[]{id}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                setValues(entity,resultSet);
            }
        });
        if(StringUtils.isEmpty(entity.getId())){
            return null;
        }
        return entity;
    }

    @Override
    public List<MsgTaskEntity> getMsgTaskList(String receiveId) {
        List<MsgTaskEntity> list = new ArrayList<>();
        String sql = "select * from t_msg_task where receiveId = ?";
        jdbcTemplate.query(sql, new String[]{receiveId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                MsgTaskEntity entity = new MsgTaskEntity();
                if(!StringUtils.isEmpty(entity.getId())){
                    list.add(entity);
                }
            }
        });
        return list;
    }

    @Override
    public boolean delMsgTaskById(String id) {
        String sql = "delete from t_msg_task where id = ?";
        jdbcTemplate.update(sql,new String[]{id});
        return true;
    }

    @Override
    public boolean delMsgTaskAdd2History(String id, ChatHistoryEntity entity) {
        delMsgTaskById(id);
        String sql = "insert into t_chat_history values(?,?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getFormId(),entity.getReceiveId(),
                entity.getFromName(),entity.getReceiveName(),entity.getCreateTime(),
                entity.getMsgType(),
                entity.getMsgDetails(),entity.getIsRead(),entity.getIsReceive()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }


    private void setValues(MsgTaskEntity entity, ResultSet resultSet)throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setFromId(resultSet.getString("fromId"));
        entity.setReceiveId(resultSet.getString("receiveId"));
        entity.setMsgType(resultSet.getString("msgType"));
        entity.setMsgDetails(resultSet.getString("msgDetails"));
        entity.setCreateTime(resultSet.getString("createTime"));
        entity.setIsRead(resultSet.getString("isRead"));
        entity.setIsReceive(resultSet.getString("isReceive"));
    }
}
