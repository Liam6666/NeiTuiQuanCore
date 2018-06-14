package com.neituiquan.work.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 简历
 *
 * form t_personal_resume
 *
 */
public class UserResumeEntity implements Serializable{

    private  String id;


    private  String userId;

    //个人介绍
    private  String introduction;

    //工作技能
    private  String workingAbility;

    //是否公开
    private  String isOpen;

    //工作年龄
    private  String workAge;

    //目标城市
    private  String targetCity;

    //期望工作
    private  String targetWork;

    //目标薪酬
    private  String targetSalary;

    //是否离职
    private  String isDeparture;

    private String sort;

    //删除标记
    private  String isDel;

    //个人成就经历
    private List<ResumeAEntity> resumeAList;

    //项目经验
    private List<ResumePEntity> resumePList;

    //学习经历
    private List<ResumeSEntity> resumeSList;

    //个人工作简历
    private List<ResumeWEntity> resumeWList;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<ResumeAEntity> getResumeAList() {
        return resumeAList;
    }

    public void setResumeAList(List<ResumeAEntity> resumeAList) {
        this.resumeAList = resumeAList;
    }

    public List<ResumePEntity> getResumePList() {
        return resumePList;
    }

    public void setResumePList(List<ResumePEntity> resumePList) {
        this.resumePList = resumePList;
    }

    public List<ResumeSEntity> getResumeSList() {
        return resumeSList;
    }

    public void setResumeSList(List<ResumeSEntity> resumeSList) {
        this.resumeSList = resumeSList;
    }

    public List<ResumeWEntity> getResumeWList() {
        return resumeWList;
    }

    public void setResumeWList(List<ResumeWEntity> resumeWList) {
        this.resumeWList = resumeWList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getWorkingAbility() {
        return workingAbility;
    }

    public void setWorkingAbility(String workingAbility) {
        this.workingAbility = workingAbility;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getWorkAge() {
        return workAge;
    }

    public void setWorkAge(String workAge) {
        this.workAge = workAge;
    }

    public String getTargetCity() {
        return targetCity;
    }

    public void setTargetCity(String targetCity) {
        this.targetCity = targetCity;
    }

    public String getTargetWork() {
        return targetWork;
    }

    public void setTargetWork(String targetWork) {
        this.targetWork = targetWork;
    }

    public String getTargetSalary() {
        return targetSalary;
    }

    public void setTargetSalary(String targetSalary) {
        this.targetSalary = targetSalary;
    }

    public String getIsDeparture() {
        return isDeparture;
    }

    public void setIsDeparture(String isDeparture) {
        this.isDeparture = isDeparture;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    /**
     * 个人获奖情况
     *
     * form t_personal_resume_a
     */
    public static class ResumeAEntity implements Serializable{

        //个人简历ID   t_personal_resume
        private  String userId;

        //获奖时间
        private  String creationTime;

        //获奖名称
        private  String rewardName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

        public String getRewardName() {
            return rewardName;
        }

        public void setRewardName(String rewardName) {
            this.rewardName = rewardName;
        }
    }

    /**
     * 项目经验
     *
     * form t_personal_resume_p
     */
    public static class ResumePEntity  implements Serializable{

        //个人简历ID   t_personal_resume
        private  String userId;

        //开始时间
        private  String startTime;

        //结束时间
        private  String endTime;

        //项目名称
        private  String projectName;

        //职责
        private  String responsibility;

        //项目描述
        private  String projectAbs;

        //链接
        private  String link;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getResponsibility() {
            return responsibility;
        }

        public void setResponsibility(String responsibility) {
            this.responsibility = responsibility;
        }

        public String getProjectAbs() {
            return projectAbs;
        }

        public void setProjectAbs(String projectAbs) {
            this.projectAbs = projectAbs;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }

    /**
     * 学习经历
     *
     * form t_personal_resume_s
     */
    public static class ResumeSEntity  implements Serializable{


        //个人简历ID   t_personal_resume
        private  String userId;


        private  String startTime;


        private  String endTime;

        //学校名字
        private  String schoolName;

        //学历
        private  String education;

        //专业
        private  String profession;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }
    }


    /**
     * 个人工作简历
     *
     * form t_personal_resume_w
     */
    public static class ResumeWEntity  implements Serializable{

        //个人简历ID   t_personal_resume
        private  String userId;


        private  String startTime;


        private  String endTime;

        //公司名
        private  String companyName;

        //所在城市
        private  String city;

        //职称
        private  String jobTitle;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
        }
    }


}
