package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.CompanyBlackListEntity;
import com.neituiquan.work.service.CompanyBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CompanyBlackListController {

    @Autowired
    private CompanyBlackListService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addToBlackList")
    public AbsEntity addToBlackList(@RequestBody CompanyBlackListEntity entity){
        return service.addToBlackList(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delBlackList")
    public AbsEntity delBlackList(@RequestParam String id){
        return service.delBlackList(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delBlackList")
    public AbsEntity getBlackList(@RequestParam String userId){
        return service.getBlackList(userId);
    }
}
