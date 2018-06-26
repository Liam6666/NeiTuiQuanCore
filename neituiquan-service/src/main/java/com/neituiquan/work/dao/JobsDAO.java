package com.neituiquan.work.dao;

import com.neituiquan.work.entity.JobListEntity;
import com.neituiquan.work.entity.JobsEntity;

import java.util.List;

public interface JobsDAO {

    boolean addJobs(JobsEntity entity);

    boolean updateJobs(JobsEntity entity);

    boolean delJobs(String id);

    JobsEntity findJobsById(String id);

    List<JobsEntity> findJobsByUserId(String userId);

    List<JobsEntity> findJobsByCompanyId(String companyId);

    List<JobListEntity> getJobsList(String city,String title);

}
