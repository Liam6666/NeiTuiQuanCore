package com.neituiquan.work.dao;

import com.neituiquan.work.entity.JobListEntity;
import com.neituiquan.work.entity.ReleaseJobsEntity;

import java.util.List;

public interface JobsDAO {

    boolean addJobs(ReleaseJobsEntity entity);

    boolean updateJobs(ReleaseJobsEntity entity);

    boolean delJobs(String id);

    ReleaseJobsEntity findJobsById(String id);

    List<ReleaseJobsEntity> findJobsByUserId(String userId,String index);

    List<ReleaseJobsEntity> findJobsByCompanyId(String companyId);

    List<JobListEntity> getJobsList(String city,String title,String index);

}
