package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.MenuDAO;
import com.neituiquan.work.entity.MenuEntity;
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
public class MenuDAOImpl implements MenuDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addMenu(MenuEntity entity) {
        String sql = "insert into t_menu_list " +
                "values(?,?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getMenuName(),entity.getUrl(),
                entity.getIconClass(),entity.getSort(),entity.getIsDel(),
                entity.getCreateTime()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateMenu(MenuEntity entity) {
        String sql = "update t_menu_list" +
                " set menuName = ?,url = ?,iconClass = ?,sort = ? " +
                "where id = ?";
        String[] params = new String[]{
                entity.getMenuName(),entity.getUrl(),entity.getIconClass(),
                entity.getSort(),entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delMenu(String id) {
        String sql = "update t_menu_list set isDel = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,id});
        return true;
    }

    @Override
    public List<MenuEntity> getAllMenu() {
        List<MenuEntity> list = new ArrayList<>();
        String sql = "select * from t_menu_list where isDel = ? group by sort asc";
        jdbcTemplate.query(sql, new String[]{FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                MenuEntity entity = new MenuEntity();
                setValues(entity,resultSet);
                if(!StringUtils.isEmpty(entity.getId())){
                 list.add(entity);
                }
            }
        });
        return list;
    }

    private void setValues(MenuEntity entity,ResultSet resultSet) throws SQLException{
        entity.setId(resultSet.getString("id"));
        entity.setMenuName(resultSet.getString("menuName"));
        entity.setUrl(resultSet.getString("url"));
        entity.setIconClass(resultSet.getString("iconClass"));
        entity.setSort(resultSet.getString("sort"));
        entity.setIsDel(resultSet.getString("isDel"));
        entity.setCreateTime(resultSet.getString("createTime"));
    }
}
