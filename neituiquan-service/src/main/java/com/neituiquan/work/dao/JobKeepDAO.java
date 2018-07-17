package com.neituiquan.work.dao;

import com.neituiquan.work.entity.JobKeepEntity;

import java.util.List;

public interface JobKeepDAO {

    boolean addJobKeep(JobKeepEntity entity);

    boolean delJobKeep(String id);

    List<JobKeepEntity> findJobKeepListByUserId(String userId);

}
