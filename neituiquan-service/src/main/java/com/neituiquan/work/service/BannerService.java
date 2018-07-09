package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.FinalData;
import com.neituiquan.work.daoImpl.BannerDAOImpl;
import com.neituiquan.work.entity.BannerEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerDAOImpl bannerDAO;

    public AbsEntity addBanner(BannerEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setIsDel(FinalData.NO_DEL);
        bannerDAO.addBanner(entity);
        return new AbsEntity();
    }


    public AbsEntity updateBanner(BannerEntity entity){
        bannerDAO.updateBanner(entity);
        return new AbsEntity();
    }


    public AbsEntity delBanner(String id){
        bannerDAO.delBanner(id);
        return new AbsEntity();
    }


    public AbsEntity findAllBanner(){
        List<BannerEntity> entityList = bannerDAO.findAllBanner();
        AbsEntity absEntity = new AbsEntity();
        absEntity.dataTotalCount = entityList.size();
        absEntity.data = entityList;
        return absEntity;
    }

    public AbsEntity findBannerById(String id){
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = bannerDAO.findBannerById(id);
        return absEntity;
    }

    public AbsEntity getBannerList(String index){
        List<BannerEntity> entityList = bannerDAO.getBannerList(index);
        AbsEntity absEntity = new AbsEntity();
        absEntity.dataTotalCount = bannerDAO.getBannerCount();
        absEntity.data = entityList;
        return absEntity;
    }
}
