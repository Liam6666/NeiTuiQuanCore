package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.JobsDAO;
import com.neituiquan.work.entity.JobsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class JobsDAOImpl implements JobsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addJobs(JobsEntity entity) {
        String sql1 = "select * from t_company where userId = ? and isDel = ?";
        jdbcTemplate.query(sql1, new String[]{entity.getUserId(),FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String companyId = resultSet.getString("id");
                String city = resultSet.getString("city");
                if(companyId != null){
                    entity.setCompanyId(companyId);
                    entity.setCity(city);
                    String sql = "insert into t_jobs values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    String[] params = new String[]{
                            entity.getId(),entity.getUserId(),entity.getCompanyId(),
                            entity.getTitle(),entity.getLabels(),entity.getEducation(),
                            entity.getCity(),entity.getWorkAge(),
                            entity.getMinSalary(),entity.getMaxSalary(),entity.getDescription(),
                            entity.getSort(),entity.getCreateTime(),entity.getIsDel()
                    };
                    jdbcTemplate.update(sql,params);
                }
            }
        });

        return true;
    }

    @Override
    public boolean updateJobs(JobsEntity entity) {
        String sql = "update t_jobs set " +
                "title = ?,labels = ?,education = ?," +
                "minSalary = ?,maxSalary = ?," +
                "description = ?,sort = ?";
        String[] params = new String[]{
                entity.getTitle(),entity.getLabels(),entity.getEducation(),
                entity.getMinSalary(),entity.getMaxSalary(),
                entity.getDescription(),entity.getSort()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delJobs(String id) {
        String sql = "update t_jobs set idDel = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,id});
        return true;
    }

    @Override
    public JobsEntity findJobsById(String id) {
        JobsEntity entity = new JobsEntity();
        String sql = "select * from t_jobs where id = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{id, FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                setValues(entity,resultSet);
            }
        });
        if(entity.getId() == null){
            return null;
        }
        return entity;
    }

    @Override
    public List<JobsEntity> findJobsByUserId(String userId) {
        List<JobsEntity> entityList = new ArrayList<>();
        String sql = "select * from t_jobs where userId = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{userId, FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JobsEntity entity = new JobsEntity();
                setValues(entity,resultSet);
                if(entity.getId() != null){
                    entityList.add(entity);
                }
            }
        });
        return entityList;
    }

    @Override
    public List<JobsEntity> findJobsByCompanyId(String companyId) {
        List<JobsEntity> entityList = new ArrayList<>();
        String sql = "select * from t_jobs where companyId = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{companyId, FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JobsEntity entity = new JobsEntity();
                setValues(entity,resultSet);
                if(entity.getId() != null){
                    entityList.add(entity);
                }
            }
        });
        return entityList;
    }


    private void setValues(JobsEntity entity,ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setUserId(resultSet.getString("userId"));
        entity.setCompanyId(resultSet.getString("companyId"));
        entity.setTitle(resultSet.getString("title"));
        entity.setLabels(resultSet.getString("labels"));
        entity.setCity(resultSet.getString("city"));
        entity.setWorkAge(resultSet.getString("workAge"));
        entity.setEducation(resultSet.getString("education"));
        entity.setMinSalary(resultSet.getString("minSalary"));
        entity.setMaxSalary(resultSet.getString("maxSalary"));
        entity.setDescription(resultSet.getString("description"));
        entity.setSort(resultSet.getString("sort"));
        entity.setCreateTime(resultSet.getString("createTime"));
        entity.setIsDel(resultSet.getString("isDel"));


    }
}
