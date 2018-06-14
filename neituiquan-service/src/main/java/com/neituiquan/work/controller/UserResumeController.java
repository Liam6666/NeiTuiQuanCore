package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.UserResumeEntity;
import com.neituiquan.work.service.UserResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserResumeController {

    @Autowired
    private UserResumeService service;


    @RequestMapping(method = RequestMethod.POST,path = "/addUserResume")
    public AbsEntity addUserResume(@RequestBody UserResumeEntity entity){
        return service.addUserResume(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateUserResume")
    public AbsEntity updateUserResume(UserResumeEntity entity){
        return service.updateUserResume(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delUserResume")
    public AbsEntity delUserResume(@RequestParam String userId){
        return service.delUserResume(userId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getUserResume")
    public AbsEntity findUserResumeByUserId(@RequestParam String userId){
        return service.findUserResumeByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getUserResumeA")
    public AbsEntity findUserResumeAByUserId(@RequestParam String userId){
        return service.findUserResumeAByUserId(userId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getUserResumeP")
    public AbsEntity findUserResumePByUserId(@RequestParam String userId){
        return service.findUserResumePByUserId(userId);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/getUserResumeS")
    public AbsEntity findUserResumeSByUserId(@RequestParam String userId){
        return service.findUserResumeSByUserId(userId);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/getUserResumeW")
    public AbsEntity findUserResumeWByUserId(@RequestParam String userId){
        return service.findUserResumeWByUserId(userId);
    }


    /**
     * http://localhost:8080/searchUserResume?workAge=3&targetWork=全栈&targetCity=苏州&_sort=workAge&_way=asc
     * @param entity
     * @param _sort 根据哪个键进行排序
     * @param _way 排序方式  desc or asc
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,path = "/searchUserResume")
    public AbsEntity searchUserResume(UserResumeEntity entity,@RequestParam String _sort,@RequestParam String _way){
        return service.searchUserResume(entity, _sort, _way);
    }

}
