package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.UserDAO;
import com.neituiquan.work.entity.UserEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean delUser(String id) {
        String sql = "delete from t_user where id = ?";
        String[] params = new String[]{id};
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateHeadImg(String id, String headImg) {
        String sql = "update t_user set headImg = ? where id = ?";
        String[] params = new String[]{headImg,id};
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateUser(UserEntity entity) {
        String sql = "update t_user " +
                "set nickName = ?,motto = ?,sex = ?,email = ? " +
                "where id = ?";
        String[] params = new String[]{
                entity.getNickName(),entity.getMotto(),entity.getSex(),entity.getEmail(),entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateLocation(UserEntity entity) {
        String sql = "update t_user set" +
                "latitude = ?,longitude = ?,accuracy = ?,province = ?,city = ?,district = ?,lastLoginTime = ?" +
                "where id = ?";
        String[] params = new String[]{
                entity.getLatitude(),entity.getLongitude(),entity.getAccuracy(),entity.getProvince(),entity.getCity(),entity.getDistrict(),entity.getLastLoginTime(),
                entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public UserEntity findUserById(String id) {
        final UserEntity entity = new UserEntity();
        String sql = "select * from t_user where id = ?";
        String[] params = new String[]{id};
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
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
    public UserEntity findUserByAccount(String account) {
        final UserEntity entity = new UserEntity();
        String sql = "select * from t_user where account = ?";
        String[] params = new String[]{account};
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
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
    public int login(UserEntity entity) {
        if(findUserByAccount(entity.getAccount()) == null){
            return -1;//账户不存在
        }
        String sql = "select * from t_user where account = ? and password = ?";
        String[] params = new String[]{
                entity.getAccount(),entity.getPassword()
        };
        final UserEntity userEntity = new UserEntity();
        jdbcTemplate.query(sql, params, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                setValues(userEntity,resultSet);
            }
        });
        if(userEntity.getId() == null){
            return -2;//账号密码不正确
        }
        sql = "update t_user set lastLoginTime = ?";
        jdbcTemplate.update(sql,new String[]{entity.getLastLoginTime()});
        return 0;//成功
    }

    @Override
    public int register(UserEntity entity) {
        if(findUserByAccount(entity.getAccount()) != null){
            return -1;//账号已存在
        }
        String sql = "insert into t_user values " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getAccount(),entity.getPassword(),
                entity.getRoleName(),entity.getHeadImg(),entity.getNickName(),
                entity.getMotto(),entity.getSex(),entity.getEmail(),
                entity.getLatitude(),entity.getLongitude(),entity.getAccuracy(),
                entity.getProvince(),entity.getCity(),entity.getDistrict(),
                entity.getLastLoginTime()
        };
        jdbcTemplate.update(sql,params);
        return 0;
    }

    @Override
    public boolean updateRole(UserEntity entity) {
        String sql = "update t_user set roleName = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{entity.getRoleName(),entity.getId()});
        return true;
    }

    @Override
    public String bindCompanyState(String id) {
        InnerObj innerObj = new InnerObj();
        String sql = "select id from t_company where userId = ? and isDel = ? ";
        jdbcTemplate.query(sql, new String[]{id, FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                innerObj.data = resultSet.getString("id");
            }
        });
        return (String)innerObj.data;
    }

    private void setValues(UserEntity entity, ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setAccount(resultSet.getString("account"));
        entity.setPassword(resultSet.getString("password"));
        entity.setRoleName(resultSet.getString("roleName"));
        entity.setHeadImg(resultSet.getString("headImg"));
        entity.setNickName(resultSet.getString("nickName"));
        entity.setMotto(resultSet.getString("motto"));
        entity.setSex(resultSet.getString("sex"));
        entity.setEmail(resultSet.getString("email"));
        entity.setLatitude(resultSet.getString("latitude"));
        entity.setLongitude(resultSet.getString("longitude"));
        entity.setAccuracy(resultSet.getString("accuracy"));
        entity.setProvince(resultSet.getString("province"));
        entity.setCity(resultSet.getString("city"));
        entity.setDistrict(resultSet.getString("district"));
        entity.setLastLoginTime(resultSet.getString("lastLoginTime"));
    }

    static class InnerObj{

        Object data;

    }
}
