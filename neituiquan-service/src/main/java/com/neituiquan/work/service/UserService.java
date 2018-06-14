package com.neituiquan.work.service;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.daoImpl.UserDAOImpl;
import com.neituiquan.work.entity.UserEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserDAOImpl userDAO;


    public AbsEntity register(UserEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setLastLoginTime(StringUtils.getCurrentTimeMillis());
        if(entity.getNickName() == null){
            entity.setNickName(entity.getAccount());
        }
        int result = userDAO.register(entity);
        AbsEntity absEntity = new AbsEntity();
        if(result == -1){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "该账号已存在";
        }else{
            absEntity.data = userDAO.findUserByAccount(entity.getAccount());
        }
        return absEntity;
    }

    public AbsEntity login(UserEntity entity){
        entity.setLastLoginTime(StringUtils.getCurrentTimeMillis());
        int result = userDAO.login(entity);
        AbsEntity absEntity = new AbsEntity();
        switch (result){
            case 0:
                absEntity.data = userDAO.findUserByAccount(entity.getAccount());
                break;
            case -1://账户不存在
                absEntity.code = FinalData.ERROR_CODE;
                absEntity.msg = "账户不存在";
                break;
            case -2://账号密码不正确
                absEntity.code = FinalData.ERROR_CODE;
                absEntity.msg = "账号密码不正确";
                break;
        }
        return absEntity;
    }

    public AbsEntity updateHeadImg(String id,String headImg){
        userDAO.updateHeadImg(id,headImg);
        return new AbsEntity();
    }

    public AbsEntity updateUser(UserEntity entity){
        userDAO.updateUser(entity);
        return new AbsEntity();
    }

    public AbsEntity updateLocation(UserEntity entity){
        userDAO.updateLocation(entity);
        return new AbsEntity();
    }
}
