package com.neituiquan.work.daoImpl;

import com.neituiquan.work.dao.JobKeepDAO;
import com.neituiquan.work.entity.JobKeepEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class JobKeepDAOImpl implements JobKeepDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addJobKeep(JobKeepEntity entity) {
        String sql = "insert into t_job_keep values (?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getUserId(),entity.getJobId(),
                entity.getJobTitle(),entity.getCompanyName(),
                entity.getMinSalary(),entity.getMaxSalary(),
                entity.getReleaseTime(),entity.getCreateTime()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delJobKeep(String id) {
        String sql = "delete from t_job_keep where id = ?";
        jdbcTemplate.update(sql,new String[]{id});
        return true;
    }

    @Override
    public List<JobKeepEntity> findJobKeepListByUserId(String userId) {
        List<JobKeepEntity> list = new ArrayList<>();
        String sql = "select * from t_job_keep where userId = ? order by CONVERT(createTime,SIGNED) desc";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JobKeepEntity entity = new JobKeepEntity();
                entity.setId(resultSet.getString("id"));
                entity.setUserId(resultSet.getString("userId"));
                entity.setJobId(resultSet.getString("jobId"));
                entity.setJobTitle(resultSet.getString("jobTitle"));
                entity.setCompanyName(resultSet.getString("companyName"));
                entity.setMinSalary(resultSet.getString("minSalary"));
                entity.setMaxSalary(resultSet.getString("maxSalary"));
                entity.setReleaseTime(resultSet.getString("releaseTime"));
                entity.setCreateTime(resultSet.getString("createTime"));
                list.add(entity);
            }
        });
        return list;
    }
}
