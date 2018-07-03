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
        if(entity.getRoleName() == null){
            entity.setRoleName(FinalData.ROLE_USER);
        }
        entity.setHeadImg(FinalData.DEF_HEAD_IMG);
        entity.setSex("男");
        entity.setMotto(FinalData.DEF_MOTTO);
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
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = userDAO.findUserById(id);
        return absEntity;
    }

    public AbsEntity updateUser(UserEntity entity){
        userDAO.updateUser(entity);
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = userDAO.findUserById(entity.getId());
        return absEntity;
    }

    public AbsEntity updateLocation(UserEntity entity){
        userDAO.updateLocation(entity);
        return new AbsEntity();
    }

    public AbsEntity updateRole(UserEntity entity) {
        AbsEntity absEntity = new AbsEntity();
        userDAO.updateRole(entity);
        absEntity.data = userDAO.findUserById(entity.getId());
        return absEntity;
    }

    public AbsEntity bindCompanyState(String id) {
        AbsEntity absEntity = new AbsEntity();
        String companyId = userDAO.bindCompanyState(id);
        if(StringUtils.isEmpty(companyId)){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "未绑定公司信息";
        }else{
            absEntity.data = companyId;
        }
        return absEntity;
    }

    public AbsEntity bindResumeState(String id) {
        AbsEntity absEntity = new AbsEntity();
        String companyId = userDAO.bindResumeState(id);
        if(StringUtils.isEmpty(companyId)){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "未绑定简历信息";
        }else{
            absEntity.data = companyId;
        }
        return absEntity;
    }
}
