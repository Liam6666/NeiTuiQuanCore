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
    public AbsEntity updateUserResume(@RequestBody UserResumeEntity entity){
        return service.updateUserResume(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateUserResumeA")
    public AbsEntity updateUserResumeA(@RequestBody UserResumeEntity.ResumeAEntity aEntity){
        return service.updateUserResumeA(aEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateUserResumeP")
    public AbsEntity updateUserResumeP(@RequestBody UserResumeEntity.ResumePEntity pEntity){
        return service.updateUserResumeP(pEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateUserResumeS")
    public AbsEntity updateUserResumeS(@RequestBody UserResumeEntity.ResumeSEntity sEntity){
        return service.updateUserResumeS(sEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateUserResumeW")
    public AbsEntity updateUserResumeW(@RequestBody UserResumeEntity.ResumeWEntity wEntity){
        return service.updateUserResumeW(wEntity);
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

    @RequestMapping(method = RequestMethod.POST,path = "/addUserResumeA")
    public AbsEntity addUserResumeA(@RequestBody UserResumeEntity.ResumeAEntity aEntity){
        return service.addUserResumeA(aEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/addUserResumeP")
    public AbsEntity addUserResumeP(@RequestBody UserResumeEntity.ResumePEntity pEntity){
        return service.addUserResumeP(pEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/addUserResumeS")
    public AbsEntity addUserResumeS(@RequestBody UserResumeEntity.ResumeSEntity sEntity){
        return service.addUserResumeS(sEntity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/addUserResumeW")
    public AbsEntity addUserResumeW(@RequestBody UserResumeEntity.ResumeWEntity wEntity){
        return service.addUserResumeW(wEntity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delUserResumeA")
    public AbsEntity delUserResumeA(@RequestParam String id){
        return service.delUserResumeA(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delUserResumeP")
    public AbsEntity delUserResumeP(@RequestParam String id){
        return service.delUserResumeP(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delUserResumeS")
    public AbsEntity delUserResumeS(@RequestParam String id){
        return service.delUserResumeS(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delUserResumeW")
    public AbsEntity delUserResumeW(@RequestParam String id){
        return service.delUserResumeW(id);
    }


    /**
     * http://localhost:8080/searchUserResume?workAge=3&targetWork=全栈&targetCity=苏州&_sort=workAge&_way=asc
     * @param entity
     * @param _sort 根据哪个键进行排序
     * @param _way 排序方式  desc or asc
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,path = "/searchUserResume")
    public AbsEntity searchUserResume(@RequestBody UserResumeEntity entity,@RequestParam String _sort,@RequestParam String _way){
        return service.searchUserResume(entity, _sort, _way);
    }

}
