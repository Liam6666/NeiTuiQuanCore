package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.daoImpl.JobKeepDAOImpl;
import com.neituiquan.work.entity.JobKeepEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobKeepService {

    @Autowired
    private JobKeepDAOImpl dao;

    public AbsEntity addJobKeep(JobKeepEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        dao.addJobKeep(entity);
        return new AbsEntity();
    }

    public AbsEntity delJobKeep(String id){
        dao.delJobKeep(id);
        return new AbsEntity();
    }

    public AbsEntity findJobKeepListByUserId(String userId){
        List<JobKeepEntity> list = dao.findJobKeepListByUserId(userId);
        AbsEntity absEntity = new AbsEntity();
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }
}
