package com.neituiquan.work.daoImpl;

import com.neituiquan.work.dao.FeedbackDAO;
import com.neituiquan.work.entity.FeedbackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDAOImpl implements FeedbackDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addFeedback(FeedbackEntity entity) {
        String sql = "insert into t_feedback values (?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getUserId(),entity.getFeedbackType(),
                entity.getMessage(),entity.getCreateTime(),entity.getIsSolve()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }
}
