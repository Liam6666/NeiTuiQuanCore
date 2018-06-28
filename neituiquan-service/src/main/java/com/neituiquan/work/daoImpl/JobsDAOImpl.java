package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.JobsDAO;
import com.neituiquan.work.entity.JobListEntity;
import com.neituiquan.work.entity.JobsEntity;
import com.neituiquan.work.utils.PageUtils;
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
                "description = ?,sort = ?,workAge = ? " +
                "where id = ?";
        String[] params = new String[]{
                entity.getTitle(),entity.getLabels(),entity.getEducation(),
                entity.getMinSalary(),entity.getMaxSalary(),
                entity.getDescription(),entity.getSort(),entity.getWorkAge(),
                entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delJobs(String id) {
        String sql = "update t_jobs set isDel = ? where id = ?";
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
        String sql = "select * from t_jobs where userId = ? and isDel = ? " +
                "group by createTime desc";
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
        String sql = "select * from t_jobs where companyId = ? and isDel = ? " +
                "group by createTime desc";
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

    @Override
    public List<JobListEntity> getJobsList(String city, String title,String index) {
        List<JobListEntity> list = new ArrayList<>();
        String sql =
                "select " +
                        "j.id,j.userId,j.title,j.labels,j.education," +
                        "j.city,j.workAge,j.minSalary,j.maxSalary," +
                        "j.createTime,c.id,c.companyName,c.address " +
                        "from t_jobs j , t_company c " +
                        "where j.isDel = ? " +
                        "and j.city like ? and j.title like ? and j.companyId = c.id " +
                        "order by j.createTime desc";
        if(StringUtils.isEmpty(title)){
            title = "";
        }
        String[] params = new String[]{
                FinalData.NO_DEL,"%"+city+"%","%"+title+"%"
        };
        sql = PageUtils.limit(sql,index);
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                JobListEntity entity = new JobListEntity();
                setJosListValues(entity,resultSet);
                if(entity.getJobsId() != null){
                    list.add(entity);
                }
            }
        });
        return list;
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


    private void setJosListValues(JobListEntity entity,ResultSet resultSet) throws SQLException{
        entity.setJobsId(resultSet.getString("j.id"));
        entity.setUserId(resultSet.getString("j.userId"));
        entity.setCompanyId(resultSet.getString("c.id"));
        entity.setTitle(resultSet.getString("j.title"));
        entity.setLabels(resultSet.getString("j.labels"));
        entity.setEducation(resultSet.getString("j.education"));
        entity.setCity(resultSet.getString("j.city"));
        entity.setWorkAge(resultSet.getString("j.workAge"));
        entity.setMinSalary(resultSet.getString("j.minSalary"));
        entity.setMaxSalary(resultSet.getString("j.maxSalary"));
        entity.setCreateTime(resultSet.getString("j.createTime"));
        entity.setCompanyName(resultSet.getString("c.companyName"));
        entity.setAddress(resultSet.getString("c.address"));
    }
}
