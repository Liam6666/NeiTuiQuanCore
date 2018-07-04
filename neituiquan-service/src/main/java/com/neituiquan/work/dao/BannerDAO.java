package com.neituiquan.work.dao;

import com.neituiquan.work.entity.BannerEntity;

import java.util.List;

public interface BannerDAO {


    boolean addBanner(BannerEntity entity);


    boolean updateBanner(BannerEntity entity);


    boolean delBanner(String id);


    List<BannerEntity> findAllBanner();

    BannerEntity findBannerById(String id);

    List<BannerEntity> getBannerList(String index);

    int getBannerCount();
}
