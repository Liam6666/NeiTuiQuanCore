package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.UserResumeDAO;
import com.neituiquan.work.entity.UserResumeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserResumeDAOImpl implements UserResumeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addUserResume(UserResumeEntity entity) {
        String sql = "insert into t_personal_resume values" +
                "(?,?,?,?" +
                ",?,?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getUserId(),entity.getBirthday(),
                entity.getEducation(),entity.getIntroduction(),
                entity.getWorkingAbility(),entity.getIsOpen(),entity.getWorkAge(),
                entity.getTargetCity(),entity.getTargetWork(),entity.getTargetSalary(),
                entity.getIsDeparture(),entity.getSort(),entity.getIsDel()
        };
        jdbcTemplate.update(sql,params);
        for(UserResumeEntity.ResumeAEntity aEntity : entity.getResumeAList()){
            sql = "insert into t_personal_resume_a values (?,?,?)";
            String[] args = new String[]{entity.getUserId(),aEntity.getCreationTime(),aEntity.getRewardName()};
            jdbcTemplate.update(sql,args);
        }

        for(UserResumeEntity.ResumePEntity pEntity : entity.getResumePList()){
            sql = "insert into t_personal_resume_p values (?,?,?,?,?,?,?)";
            String[] args = new String[]{
                    entity.getUserId(),pEntity.getStartTime(),pEntity.getEndTime(),
                    pEntity.getProjectName(),pEntity.getResponsibility(),
                    pEntity.getProjectAbs(),pEntity.getLink()
            };
            jdbcTemplate.update(sql,args);
        }

        for(UserResumeEntity.ResumeSEntity sEntity : entity.getResumeSList()){
            sql = "insert into t_personal_resume_s values (?,?,?,?,?,?)";
            String[] args = new String[]{
                    entity.getUserId(),sEntity.getStartTime(),sEntity.getEndTime(),
                    sEntity.getSchoolName(),sEntity.getEducation(),sEntity.getProfession()
            };
            jdbcTemplate.update(sql,args);
        }

        for(UserResumeEntity.ResumeWEntity wEntity : entity.getResumeWList()){
            sql = "insert into t_personal_resume_w values (?,?,?,?,?,?)";
            String[] args = new String[]{
                    entity.getUserId(),wEntity.getStartTime(),wEntity.getEndTime(),
                    wEntity.getCompanyName(),wEntity.getCity(),wEntity.getJobTitle()
            };
            jdbcTemplate.update(sql,args);
        }

        return true;
    }

    @Override
    public boolean updateUserResume(UserResumeEntity entity) {
        String sql = "update t_personal_resume " +
                "set birthday = ?,education = ?,introduction = ?," +
                "workingAbility = ?,workAge = ?,targetCity = ?," +
                "targetWork = ?,targetSalary = ? " +
                "where id = ?";
        String[] params = new String[]{
                entity.getBirthday(),entity.getEducation(),entity.getIntroduction(),
                entity.getWorkingAbility(),entity.getWorkAge(),entity.getTargetCity(),
                entity.getTargetWork(),entity.getTargetSalary(),entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateUserResumeA(UserResumeEntity.ResumeAEntity aEntity) {
        String sql = "update t_personal_resume_a " +
                "set creationTime = ?,rewardName = ? " +
                "where id = ?";
        String[] params = new String[]{
                aEntity.getCreationTime(),aEntity.getRewardName(),aEntity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateUserResumeP(UserResumeEntity.ResumePEntity pEntity) {
        String sql = "update t_personal_resume_p " +
                "set startTime = ?,endTime = ?,projectName = ?,responsibility = ?," +
                "projectAbs = ?,link = ? " +
                "where id = ?";
        String[] params = new String[]{
                pEntity.getStartTime(),pEntity.getEndTime(),pEntity.getProjectName(),pEntity.getResponsibility(),
                pEntity.getProjectAbs(),pEntity.getLink(),pEntity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateUserResumeS(UserResumeEntity.ResumeSEntity sEntity) {
        String sql = "update t_personal_resume_s " +
                "set startTime = ?,endTime = ?,schoolName = ?," +
                "education = ?,profession = ? " +
                "where id = ?";
        String[] params = new String[]{
                sEntity.getStartTime(),sEntity.getEndTime(),sEntity.getSchoolName(),
                sEntity.getEducation(),sEntity.getProfession(),sEntity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateUserResumeW(UserResumeEntity.ResumeWEntity wEntity) {
        String sql = "update t_personal_resume_w " +
                "set startTime = ?,endTime = ?,companyName = ?," +
                "city = ?,jobTitle = ? " +
                "where id = ?";
        String[] params = new String[]{
                wEntity.getStartTime(),wEntity.getEndTime(),wEntity.getCompanyName(),
                wEntity.getCity(),wEntity.getJobTitle(),wEntity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }


    @Override
    public boolean delUserResume(String userId) {
        String sql = "update t_personal_resume set isDel = ? where userId = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,userId});
        return true;
    }

    @Override
    public UserResumeEntity findUserResumeByUserId(String userId) {
        UserResumeEntity entity = new UserResumeEntity();
        String sql = "select * from t_personal_resume where userId = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{userId,FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                setValues(entity,resultSet);
                entity.setResumeAList(findUserResumeAByUserId(userId));
                entity.setResumePList(findUserResumePByUserId(userId));
                entity.setResumeSList(findUserResumeSByUserId(userId));
                entity.setResumeWList(findUserResumeWByUserId(userId));
            }
        });
        if(entity.getId() == null){
            return null;
        }
        return entity;
    }

    @Override
    public List<UserResumeEntity.ResumeAEntity> findUserResumeAByUserId(String userId) {
        List<UserResumeEntity.ResumeAEntity> aEntityList = new ArrayList<>();
        String sql = "select * from t_personal_resume_a where userId = ?";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                UserResumeEntity.ResumeAEntity aEntity = new UserResumeEntity.ResumeAEntity();
                aEntity.setId(resultSet.getString("id"));
                aEntity.setUserId(resultSet.getString("userId"));
                aEntity.setCreationTime(resultSet.getString("creationTime"));
                aEntity.setRewardName(resultSet.getString("rewardName"));
                aEntityList.add(aEntity);
            }
        });
        return aEntityList;
    }

    @Override
    public List<UserResumeEntity.ResumePEntity> findUserResumePByUserId(String userId) {
        List<UserResumeEntity.ResumePEntity> pEntityList = new ArrayList<>();
        String sql = "select * from t_personal_resume_p where userId = ?";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                UserResumeEntity.ResumePEntity pEntity = new UserResumeEntity.ResumePEntity();
                pEntity.setId(resultSet.getString("id"));
                pEntity.setUserId(resultSet.getString("userId"));
                pEntity.setStartTime(resultSet.getString("startTime"));
                pEntity.setEndTime(resultSet.getString("endTime"));
                pEntity.setProjectName(resultSet.getString("projectName"));
                pEntity.setResponsibility(resultSet.getString("responsibility"));
                pEntity.setProjectAbs(resultSet.getString("projectAbs"));
                pEntity.setLink(resultSet.getString("link"));
                pEntityList.add(pEntity);
            }
        });
        return pEntityList;
    }

    @Override
    public List<UserResumeEntity.ResumeSEntity> findUserResumeSByUserId(String userId) {
        List<UserResumeEntity.ResumeSEntity> pEntityList = new ArrayList<>();
        String sql = "select * from t_personal_resume_s where userId = ?";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                UserResumeEntity.ResumeSEntity sEntity = new UserResumeEntity.ResumeSEntity();
                sEntity.setId(resultSet.getString("id"));
                sEntity.setUserId(resultSet.getString("userId"));
                sEntity.setStartTime(resultSet.getString("startTime"));
                sEntity.setEndTime(resultSet.getString("endTime"));
                sEntity.setSchoolName(resultSet.getString("schoolName"));
                sEntity.setEducation(resultSet.getString("education"));
                sEntity.setProfession(resultSet.getString("profession"));
                pEntityList.add(sEntity);
            }
        });
        return pEntityList;
    }

    @Override
    public List<UserResumeEntity.ResumeWEntity> findUserResumeWByUserId(String userId) {
        List<UserResumeEntity.ResumeWEntity> wEntityList = new ArrayList<>();
        String sql = "select * from t_personal_resume_w where userId = ?";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                UserResumeEntity.ResumeWEntity wEntity = new UserResumeEntity.ResumeWEntity();
                wEntity.setId(resultSet.getString("id"));
                wEntity.setUserId(resultSet.getString("userId"));
                wEntity.setStartTime(resultSet.getString("startTime"));
                wEntity.setEndTime(resultSet.getString("endTime"));
                wEntity.setCompanyName(resultSet.getString("companyName"));
                wEntity.setCity(resultSet.getString("city"));
                wEntity.setJobTitle(resultSet.getString("jobTitle"));
                wEntityList.add(wEntity);
            }
        });
        return wEntityList;
    }

    @Override
    public List<UserResumeEntity> searchUserResume(UserResumeEntity entity,String sort,String way) {
        StringBuffer sql = new StringBuffer(
                "select * from t_personal_resume where isDel = ?");
        List<String> paramsList = new ArrayList<>();
        paramsList.add(FinalData.NO_DEL);
        if(entity.getWorkAge() != null){
            sql.append(" and workAge = ?");
            paramsList.add(entity.getWorkAge());
        }
        if(entity.getTargetCity() != null){
            sql.append(" and targetCity like ?");
            paramsList.add("%"+entity.getTargetCity()+"%");
        }
        if(entity.getTargetWork() != null){
            sql.append(" and targetWork like ?");
            paramsList.add("%"+entity.getTargetWork()+"%");
        }
//        if(entity.getTargetSalary() != null){
//            stringBuffer.append(" and targetSalary = ?");
//        }
        sql.append(" order by ? ");
        paramsList.add(sort);
        sql.append(way);
        String[] params = new String[paramsList.size()];
        int count = 0;
        for(String p : paramsList){
            params[count] = p;
//            System.err.println(p);
            count ++;
        }
        List<UserResumeEntity> entityList = new ArrayList<>();
        jdbcTemplate.query(sql.toString(), params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                UserResumeEntity resumeEntity = new UserResumeEntity();
                setValues(resumeEntity,resultSet);
                entityList.add(resumeEntity);
            }
        });
//        System.err.println(sql);
        return entityList;
    }


    private void setValues(UserResumeEntity entity,ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setUserId(resultSet.getString("userId"));
        entity.setBirthday(resultSet.getString("birthday"));
        entity.setEducation(resultSet.getString("education"));
        entity.setIntroduction(resultSet.getString("introduction"));
        entity.setWorkingAbility(resultSet.getString("workingAbility"));
        entity.setIsOpen(resultSet.getString("isOpen"));
        entity.setWorkAge(resultSet.getString("workAge"));
        entity.setTargetCity(resultSet.getString("targetCity"));
        entity.setTargetWork(resultSet.getString("targetWork"));
        entity.setTargetSalary(resultSet.getString("targetSalary"));
        entity.setIsDeparture(resultSet.getString("isDeparture"));
        entity.setSort(resultSet.getString("sort"));
        entity.setIsDel(resultSet.getString("isDel"));
    }
}
