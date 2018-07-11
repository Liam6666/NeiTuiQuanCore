package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.FinalData;
import com.neituiquan.work.daoImpl.JobsDAOImpl;
import com.neituiquan.work.entity.JobListEntity;
import com.neituiquan.work.entity.ReleaseJobsEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobsService {

    @Autowired
    private JobsDAOImpl jobsDAO;

    public AbsEntity addJobs(ReleaseJobsEntity entity){
        entity.setIsDel(FinalData.NO_DEL);
        entity.setSort("0");
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        entity.setState(FinalData.VERIFYING);
        jobsDAO.addJobs(entity);
        return new AbsEntity();
    }

    public AbsEntity updateJobs(ReleaseJobsEntity entity){
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

    public AbsEntity findJobsByUserId(String userId,String index){
        AbsEntity absEntity = new AbsEntity();
        List<ReleaseJobsEntity> list = jobsDAO.findJobsByUserId(userId,index);
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }

    public AbsEntity findJobsByCompanyId(String companyId){
        AbsEntity absEntity = new AbsEntity();
        List<ReleaseJobsEntity> list = jobsDAO.findJobsByCompanyId(companyId);
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }

    public AbsEntity getJobsList(String city, String title,String index){
        AbsEntity absEntity = new AbsEntity();
        List<JobListEntity> list = jobsDAO.getJobsList(city,title,index);
        absEntity.dataTotalCount = list.size();
        absEntity.data = list;
        return absEntity;
    }
}
