package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.BannerEntity;
import com.neituiquan.work.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BannerController {

    @Autowired
    private BannerService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addBanner")
    public AbsEntity addBanner(@RequestBody BannerEntity entity){
        return service.addBanner(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateBanner")
    public AbsEntity updateBanner(@RequestBody BannerEntity entity){
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


    @RequestMapping(method = RequestMethod.GET,path = "/findBannerById")
    public AbsEntity findBannerById(@RequestParam String id){
        return service.findBannerById(id);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/getBannerList")
    public AbsEntity getBannerList(@RequestParam String index){
        return service.getBannerList(index);
    }

}
