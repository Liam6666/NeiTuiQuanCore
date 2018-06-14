package com.neituiquan.work.daoImpl;

import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.dao.BannerDAO;
import com.neituiquan.work.entity.BannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class BannerDAOImpl implements BannerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addBanner(BannerEntity entity) {
        String sql = "insert into t_banner values(?,?,?,?,?,?)";
        String[] params = new String[]{
                entity.getId(),entity.getImgUrl(),entity.getTitleMsg(),
                entity.getSort(),entity.getLinkUrl(),entity.getIsDel()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean updateBanner(BannerEntity entity) {
        String sql = "update t_banner set" +
                " imgUrl = ?,titleMsg = ?,sort = ?,linkUrl = ?" +
                " where id = ?";
        String[] params = new String[]{
                entity.getImgUrl(),entity.getTitleMsg(),
                entity.getSort(),entity.getLinkUrl(),
                entity.getId()
        };
        jdbcTemplate.update(sql,params);
        return true;
    }

    @Override
    public boolean delBanner(String id) {
        String sql = "update t_banner set isDel = ? where id = ?";
        jdbcTemplate.update(sql,new String[]{FinalData.DEL,id});
        return true;
    }

    @Override
    public List<BannerEntity> findAllBanner() {
        List<BannerEntity> entityList = new ArrayList<>();
        String sql = "select * from t_banner where isDel = ? order by sort";
        jdbcTemplate.query(sql, new String[]{FinalData.NO_DEL}, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                BannerEntity entity = new BannerEntity();
                entity.setId(resultSet.getString("id"));
                entity.setImgUrl(resultSet.getString("imgUrl"));
                entity.setTitleMsg(resultSet.getString("titleMsg"));
                entity.setSort(resultSet.getString("sort"));
                entity.setLinkUrl(resultSet.getString("linkUrl"));
                entity.setIsDel(resultSet.getString("isDel"));
                entityList.add(entity);
            }
        });
        return entityList;
    }
}
