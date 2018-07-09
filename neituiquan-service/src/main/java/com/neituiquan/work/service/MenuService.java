package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.FinalData;
import com.neituiquan.work.daoImpl.MenuDAOImpl;
import com.neituiquan.work.entity.MenuEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuDAOImpl menuDAO;


    public AbsEntity addMenu(MenuEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setIsDel(FinalData.NO_DEL);
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        menuDAO.addMenu(entity);
        return new AbsEntity();
    }


    public AbsEntity updateMenu(MenuEntity entity){
        menuDAO.updateMenu(entity);
        return new AbsEntity();
    }


    public AbsEntity delMenu(String id){
        menuDAO.delMenu(id);
        return new AbsEntity();
    }

    public AbsEntity getAllMenu(){
        List<MenuEntity> list = menuDAO.getAllMenu();
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }
}
