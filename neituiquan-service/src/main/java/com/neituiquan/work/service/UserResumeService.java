package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.daoImpl.UserResumeDAOImpl;
import com.neituiquan.work.entity.UserResumeEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserResumeService {

    @Autowired
    private UserResumeDAOImpl resumeDAO;


    public AbsEntity addUserResume(UserResumeEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setIsDel(FinalData.NO_DEL);
        entity.setIsOpen(FinalData.OPEN);
        entity.setIsDeparture(FinalData.DEPARTURE);
        resumeDAO.addUserResume(entity);
        for(UserResumeEntity.ResumeAEntity aEntity : entity.getResumeAList()){
            aEntity.setId(StringUtils.getUUID());
        }
        for(UserResumeEntity.ResumePEntity pEntity : entity.getResumePList()){
            pEntity.setId(StringUtils.getUUID());
        }
        for(UserResumeEntity.ResumeSEntity sEntity : entity.getResumeSList()){
            sEntity.setId(StringUtils.getUUID());
        }
        for(UserResumeEntity.ResumeWEntity wEntity : entity.getResumeWList()){
            wEntity.setId(StringUtils.getUUID());
        }
        return new AbsEntity();
    }

    public AbsEntity updateUserResume(UserResumeEntity entity){
        resumeDAO.updateUserResume(entity);
        return new AbsEntity();
    }


    public AbsEntity updateUserResumeA(UserResumeEntity.ResumeAEntity aEntity){
        resumeDAO.updateUserResumeA(aEntity);
        return new AbsEntity();
    }

    public AbsEntity updateUserResumeP(UserResumeEntity.ResumePEntity pEntity){
        resumeDAO.updateUserResumeP(pEntity);
        return new AbsEntity();
    }

    public AbsEntity updateUserResumeS(UserResumeEntity.ResumeSEntity sEntity){
        resumeDAO.updateUserResumeS(sEntity);
        return new AbsEntity();
    }

    public AbsEntity updateUserResumeW(UserResumeEntity.ResumeWEntity wEntity){
        resumeDAO.updateUserResumeW(wEntity);
        return new AbsEntity();
    }

    public AbsEntity delUserResume(String userId){
        resumeDAO.delUserResume(userId);
        return new AbsEntity();
    }

    public AbsEntity findUserResumeByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        UserResumeEntity entity = resumeDAO.findUserResumeByUserId(userId);
        absEntity.data = entity;
        return absEntity;
    }

    public AbsEntity findUserResumeAByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<UserResumeEntity.ResumeAEntity> list = resumeDAO.findUserResumeAByUserId(userId);
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }

    public AbsEntity findUserResumePByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<UserResumeEntity.ResumePEntity> list = resumeDAO.findUserResumePByUserId(userId);
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }

    public AbsEntity findUserResumeSByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<UserResumeEntity.ResumeSEntity> list = resumeDAO.findUserResumeSByUserId(userId);
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }

    public AbsEntity findUserResumeWByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<UserResumeEntity.ResumeWEntity> list = resumeDAO.findUserResumeWByUserId(userId);
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }

    public AbsEntity searchUserResume(UserResumeEntity entity,String sort,String way){
        List<UserResumeEntity> list = resumeDAO.searchUserResume(entity,sort,way);
        AbsEntity absEntity = new AbsEntity();
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }
}
