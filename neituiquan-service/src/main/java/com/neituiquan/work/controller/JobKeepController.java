package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.JobKeepEntity;
import com.neituiquan.work.service.JobKeepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobKeepController {

    @Autowired
    private JobKeepService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addJobKeep")
    public AbsEntity addJobKeep(@RequestBody JobKeepEntity entity){
        return service.addJobKeep(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delJobKeep")
    public AbsEntity delJobKeep(@RequestParam String id){
        return service.delJobKeep(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/findJobKeepListByUserId")
    public AbsEntity findJobKeepListByUserId(@RequestParam String userId){
        return service.findJobKeepListByUserId(userId);
    }
}
