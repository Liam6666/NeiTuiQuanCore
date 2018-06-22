package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.daoImpl.JobsDAOImpl;
import com.neituiquan.work.entity.JobsEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsService {

    @Autowired
    private JobsDAOImpl jobsDAO;

    public AbsEntity addJobs(JobsEntity entity){
        entity.setIsDel(FinalData.NO_DEL);
        entity.setSort("0");
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        jobsDAO.addJobs(entity);
        return new AbsEntity();
    }

    public AbsEntity updateJobs(JobsEntity entity){
        jobsDAO.updateJobs(entity);
        return new AbsEntity();
    }

    public AbsEntity delJobs(String id){
        jobsDAO.delJobs(id);
        return new AbsEntity();
    }

    public AbsEntity findJobsById(String id){
        AbsEntity absEntity = new AbsEntity();
        absEntity.data = jobsDAO.findJobsById(id);
        return absEntity;
    }

    public AbsEntity findJobsByUserId(String userId){
        AbsEntity absEntity = new AbsEntity();
        List<JobsEntity> list = jobsDAO.findJobsByUserId(userId);
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }

    public AbsEntity findJobsByCompanyId(String companyId){
        AbsEntity absEntity = new AbsEntity();
        List<JobsEntity> list = jobsDAO.findJobsByCompanyId(companyId);
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }
}
