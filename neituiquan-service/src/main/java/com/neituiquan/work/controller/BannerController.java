package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.BannerEntity;
import com.neituiquan.work.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BannerController {

    @Autowired
    private BannerService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addBanner")
    public AbsEntity addBanner(BannerEntity entity){
        return service.addBanner(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateBanner")
    public AbsEntity updateBanner(BannerEntity entity){
        return service.updateBanner(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delBanner")
    public AbsEntity delBanner(@RequestParam String id){
        return service.delBanner(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getAllBanner")
    public AbsEntity getAllBanner(){
        return service.findAllBanner();
    }
}
