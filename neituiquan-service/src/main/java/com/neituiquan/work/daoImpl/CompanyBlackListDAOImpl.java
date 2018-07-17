package com.neituiquan.work.daoImpl;

import com.neituiquan.work.dao.CompanyBlackListDAO;
import com.neituiquan.work.entity.CompanyBlackListEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyBlackListDAOImpl implements CompanyBlackListDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addToBlackList(CompanyBlackListEntity entity) {
        StringBuffer companyName = new StringBuffer();
        String sql = "select companyName from t_company where id = ?";
        jdbcTemplate.query(sql, new String[]{entity.getCompanyId()}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                companyName.append(resultSet.getString("companyName"));
            }
        });
        sql = "insert into t_company_blacklist values (?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),
                entity.getUserId(),
                entity.getCompanyId(),
                companyName.toString(),
                entity.getCreateTime()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delBlackList(String id) {
        String sql = "delete from t_company_blacklist where id = ?";
        jdbcTemplate.update(sql,new String[]{id});
        return true;
    }

    @Override
    public List<CompanyBlackListEntity> getBlackList(String userId) {
        List<CompanyBlackListEntity> list = new ArrayList<>();
        String sql = "select * from t_company_blacklist where userId = ? order by CONVERT(createTime,SIGNED) desc";
        jdbcTemplate.query(sql, new String[]{userId}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {

                CompanyBlackListEntity entity = new CompanyBlackListEntity();
                entity.setId(resultSet.getString("id"));
                entity.setUserId(resultSet.getString("userId"));
                entity.setCompanyId(resultSet.getString("companyId"));
                entity.setCompanyName(resultSet.getString("companyName"));
                entity.setCreateTime(resultSet.getString("createTime"));
                list.add(entity);
            }
        });
        return list;
    }
}
