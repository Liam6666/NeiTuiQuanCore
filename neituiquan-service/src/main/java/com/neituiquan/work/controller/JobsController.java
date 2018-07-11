package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.ReleaseJobsEntity;
import com.neituiquan.work.service.JobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class JobsController {

    @Autowired
    private JobsService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addJobs")
    public AbsEntity addJobs(@RequestBody ReleaseJobsEntity entity){
        return service.addJobs(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateJobs")
    public AbsEntity updateJobs(@RequestBody ReleaseJobsEntity entity){
        return service.updateJobs(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delJobs")
    public AbsEntity delJobs(@RequestParam String id){
        return service.delJobs(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/findJobsById")
    public AbsEntity findJobsById(@RequestParam String id){
        return service.findJobsById(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/findJobsByUserId")
    public AbsEntity findJobsByUserId(@RequestParam String userId,@RequestParam String index){
        return service.findJobsByUserId(userId,index);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/findJobsByCompanyId")
    public AbsEntity findJobsByCompanyId(@RequestParam String companyId){
        return service.findJobsByCompanyId(companyId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getJobsList")
    public AbsEntity getJobsList(@RequestParam String city,@RequestParam String title,@RequestParam String index){
        return service.getJobsList(city, title,index);
    }
}
