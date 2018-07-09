package com.neituiquan.work.daoImpl;

import com.neituiquan.work.FinalData;
import com.neituiquan.work.dao.CompanyDAO;
import com.neituiquan.work.entity.CompanyEntity;
import com.neituiquan.work.entity.CompanyImgEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class CompanyDAOImpl implements CompanyDAO {


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public boolean addCompanyImg(List<CompanyImgEntity> imgEntityList) {
        for(CompanyImgEntity entity : imgEntityList){
            String sql = "insert into t_company_img values " +
                    "(?,?,?,?,?)";
            String[] params = new String[]{
                    entity.getId(),entity.getCompanyId(),entity.getImgUrl(),entity.getSort(),FinalData.NO_DEL
            };
            jdbcTemplate.update(sql,params);
        }
        return true;
    }

    @Override
    public boolean delCompanyImg(String id) {
        String sql = "update t_company_img set isDel = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,id});
        return true;
    }

    @Override
    public boolean delCompanyAllImg(String companyId) {
        String sql = "update t_company_img set isDel = ? where companyId = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,companyId});
        return false;
    }

    @Override
    public boolean addCompany(CompanyEntity entity) {
        String sql = "insert into t_company values " +
                "(?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getUserId(),entity.getCompanyName(),
                entity.getProvince(),entity.getCity(),entity.getAddress(),
                entity.getIntroduce(),entity.getCreationTime(),entity.getPeopleNum(),
                entity.getLinkUrl(),entity.getLabels(),entity.getIsDel()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delCompany(String id) {
        String sql = "update t_company set isDel = ? where id = ?";
        String[] params = new String[]{FinalData.DEL,id};
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateCompany(CompanyEntity entity) {
        String sql = "update t_company set " +
                "companyName = ?,province = ?,city = ?,address = ?,introduce = ? " +
                "creationTime = ?,peopleNum = ?,linkUrl = ?,labels = ? " +
                "where id = ?";
        String[] params = new String[]{
                entity.getCompanyName(),entity.getProvince(),entity.getCity(),entity.getAddress(),entity.getIntroduce(),
                entity.getCreationTime(),entity.getPeopleNum(),entity.getLinkUrl(),entity.getLabels(),
                entity.getIsDel()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public CompanyEntity findCompanyById(String id) {
        CompanyEntity entity = new CompanyEntity();
        String sql = "select * from t_company where id = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{id,FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                setValues(entity,resultSet);
            }
        });
        if(entity.getId() != null){
            return entity;
        }
        return null;
    }

    @Override
    public List<CompanyEntity> findCompanyByUserId(String userId) {
        List<CompanyEntity> entityList = new ArrayList<>();
        String sql = "select * from t_company where userId = ? and isDel = ?";
        jdbcTemplate.query(sql, new String[]{userId,FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                CompanyEntity entity = new CompanyEntity();
                setValues(entity,resultSet);
                if(entity.getId() != null){
                    entityList.add(entity);
                }
            }
        });
        return entityList;
    }

    private void setValues(CompanyEntity entity, ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setUserId(resultSet.getString("userId"));
        entity.setCompanyName(resultSet.getString("companyName"));
        entity.setProvince(resultSet.getString("province"));
        entity.setCity(resultSet.getString("city"));
        entity.setAddress(resultSet.getString("address"));
        entity.setIntroduce(resultSet.getString("introduce"));
        entity.setCreationTime(resultSet.getString("creationTime"));
        entity.setPeopleNum(resultSet.getString("peopleNum"));
        entity.setLinkUrl(resultSet.getString("linkUrl"));
        entity.setLabels(resultSet.getString("labels"));
        entity.setIsDel(resultSet.getString("isDel"));
        String sql = "select * from t_company_img where companyId = ? and isDel = ? ORDER BY sort";
        jdbcTemplate.query(sql, new String[]{entity.getId(),FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                CompanyImgEntity imgEntity = new CompanyImgEntity();
                imgEntity.setId(resultSet.getString("id"));
                imgEntity.setCompanyId(resultSet.getString("companyId"));
                imgEntity.setImgUrl(resultSet.getString("imgUrl"));
                imgEntity.setSort(resultSet.getString("sort"));
                imgEntity.setIsDel(resultSet.getString("isDel"));
                entity.getImgList().add(imgEntity);
            }
        });

        sql = "select AVG(score) from t_company_score where companyId = ?";
        jdbcTemplate.query(sql, new String[]{entity.getId()}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                entity.setScore(resultSet.getInt(1));
            }
        });
        sql = "select count(*) from t_company_score where companyId = ?";
        jdbcTemplate.query(sql, new String[]{entity.getId()}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                entity.setScoreCount(resultSet.getInt(1));
            }
        });
    }

}
