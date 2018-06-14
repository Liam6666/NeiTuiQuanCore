package com.neituiquan.work.service;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.daoImpl.CompanyDAOImpl;
import com.neituiquan.work.entity.CompanyEntity;
import com.neituiquan.work.entity.CompanyImgEntity;
import com.neituiquan.work.utils.StringUtils;
import org.omg.CORBA.IMP_LIMIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyDAOImpl companyDAO;

    public AbsEntity addCompanyImg(List<CompanyImgEntity> imgEntityList){
        for(CompanyImgEntity entity : imgEntityList){
            entity.setId(StringUtils.getUUID());
            entity.setIsDel(FinalData.NO_DEL);
        }
        companyDAO.addCompanyImg(imgEntityList);
        return new AbsEntity();
    }

    public AbsEntity delCompanyImg(String id){
        companyDAO.delCompanyImg(id);
        return new AbsEntity();
    }

    public AbsEntity delCompanyAllImg(String companyId){
        companyDAO.delCompanyAllImg(companyId);
        return new AbsEntity();
    }

    public AbsEntity addCompany(CompanyEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setIsDel(FinalData.NO_DEL);
        companyDAO.addCompany(entity);
        return new AbsEntity();
    }

    public AbsEntity delCompany(String id){
        companyDAO.delCompany(id);
        return new AbsEntity();
    }

    public AbsEntity updateCompany(CompanyEntity entity){
        companyDAO.updateCompany(entity);
        return new AbsEntity();
    }

    public AbsEntity findCompanyById(String id){
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = companyDAO.findCompanyById(id);
        return absEntity;
    }


    public AbsEntity findCompanyByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<CompanyEntity> entityList = companyDAO.findCompanyByUserId(userId);
        absEntity.data = entityList;
        absEntity.dataTotalCount = entityList.size();
        return absEntity;
    }
}
