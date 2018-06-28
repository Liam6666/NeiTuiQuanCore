package com.neituiquan.work.dao;

import com.neituiquan.work.entity.UserEntity;

public interface UserDAO {

    boolean delUser(String id);

    boolean updateHeadImg(String id,String headImg);

    boolean updateUser(UserEntity entity);

    boolean updateLocation(UserEntity entity);

    UserEntity findUserById(String id);

    UserEntity findUserByAccount(String account);

    int login(UserEntity entity);

    int register(UserEntity entity);

    boolean updateRole(UserEntity entity);

    /**
     * 判断用户是否绑定了公司信息
     * @param id
     * @return companyId
     */
    String bindCompanyState(String id);
}
