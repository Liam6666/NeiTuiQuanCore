package com.neituiquan.work.dao;

import com.neituiquan.work.entity.CompanyBlackListEntity;

import java.util.List;

public interface CompanyBlackListDAO  {


    boolean addToBlackList(CompanyBlackListEntity entity);


    boolean delBlackList(String id);


    List<CompanyBlackListEntity> getBlackList(String userId);


}
