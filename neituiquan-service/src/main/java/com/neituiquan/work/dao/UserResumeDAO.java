package com.neituiquan.work.dao;


import com.neituiquan.work.entity.UserResumeEntity;

import java.util.List;

public interface UserResumeDAO {


    boolean addUserResume(UserResumeEntity entity);

    boolean updateUserResume(UserResumeEntity entity);

    boolean updateUserResumeA(UserResumeEntity.ResumeAEntity aEntity);

    boolean updateUserResumeP(UserResumeEntity.ResumePEntity pEntity);

    boolean updateUserResumeS(UserResumeEntity.ResumeSEntity sEntity);

    boolean updateUserResumeW(UserResumeEntity.ResumeWEntity wEntity);

    boolean delUserResume(String userId);

    UserResumeEntity findUserResumeByUserId(String userId);

    List<UserResumeEntity.ResumeAEntity> findUserResumeAByUserId(String userId);

    List<UserResumeEntity.ResumePEntity> findUserResumePByUserId(String userId);

    List<UserResumeEntity.ResumeSEntity> findUserResumeSByUserId(String userId);

    List<UserResumeEntity.ResumeWEntity> findUserResumeWByUserId(String userId);

    boolean addUserResumeA(UserResumeEntity.ResumeAEntity aEntity);

    boolean addUserResumeP(UserResumeEntity.ResumePEntity pEntity);

    boolean addUserResumeS(UserResumeEntity.ResumeSEntity sEntity);

    boolean addUserResumeW(UserResumeEntity.ResumeWEntity wEntity);

    boolean delUserResumeA(String id);

    boolean delUserResumeP(String id);

    boolean delUserResumeS(String id);

    boolean delUserResumeW(String id);

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
