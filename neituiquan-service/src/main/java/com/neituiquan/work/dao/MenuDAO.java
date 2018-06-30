package com.neituiquan.work.dao;

import com.neituiquan.work.entity.MenuEntity;

import java.util.List;

public interface MenuDAO {

    boolean addMenu(MenuEntity entity);


    boolean updateMenu(MenuEntity entity);


    boolean delMenu(String id);

    List<MenuEntity> getAllMenu();
}
