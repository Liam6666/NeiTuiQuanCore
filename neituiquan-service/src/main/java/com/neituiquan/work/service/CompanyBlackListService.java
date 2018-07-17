package com.neituiquan.work.service;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.daoImpl.CompanyBlackListDAOImpl;
import com.neituiquan.work.entity.CompanyBlackListEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyBlackListService {

    @Autowired
    private CompanyBlackListDAOImpl dao;

    public AbsEntity addToBlackList(CompanyBlackListEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        dao.addToBlackList(entity);
        return new AbsEntity();
    }


    public AbsEntity delBlackList(String id){
        dao.delBlackList(id);
        return new AbsEntity();
    }


    public AbsEntity getBlackList(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<CompanyBlackListEntity> list = dao.getBlackList(userId);
        absEntity.data = list;
        absEntity.dataTotalCount = list.size();
        return absEntity;
    }
}
