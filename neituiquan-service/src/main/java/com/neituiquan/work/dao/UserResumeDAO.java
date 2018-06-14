package com.neituiquan.work.dao;


import com.neituiquan.work.entity.UserResumeEntity;

import java.util.List;

public interface UserResumeDAO {


    boolean addUserResume(UserResumeEntity entity);

    boolean updateUserResume(UserResumeEntity entity);

    boolean delUserResume(String userId);

    UserResumeEntity findUserResumeByUserId(String userId);

    List<UserResumeEntity.ResumeAEntity> findUserResumeAByUserId(String userId);

    List<UserResumeEntity.ResumePEntity> findUserResumePByUserId(String userId);

    List<UserResumeEntity.ResumeSEntity> findUserResumeSByUserId(String userId);

    List<UserResumeEntity.ResumeWEntity> findUserResumeWByUserId(String userId);


    /**
     * 条件搜索
     *  workAge
     *  targetCity
     *  targetWork
     *  isDeparture
     *
     *  page
     *  size
     *
     *  sort targetSalary
     *  sort workAge
     *  sort
     *
     *  way asc  desc
     */
    List<UserResumeEntity> searchUserResume(UserResumeEntity entity,String sort,String way);

}
