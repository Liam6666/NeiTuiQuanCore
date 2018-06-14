package com.neituiquan.work.dao;

import com.neituiquan.work.entity.CompanyEntity;
import com.neituiquan.work.entity.CompanyImgEntity;

import java.util.List;

public interface CompanyDAO {

    boolean addCompanyImg(List<CompanyImgEntity> imgEntityList);

    boolean delCompanyImg(String id);

    boolean delCompanyAllImg(String companyId);

    boolean addCompany(CompanyEntity entity);

    boolean delCompany(String id);

    boolean updateCompany(CompanyEntity entity);

    CompanyEntity findCompanyById(String id);

    List<CompanyEntity> findCompanyByUserId(String userId);
}
