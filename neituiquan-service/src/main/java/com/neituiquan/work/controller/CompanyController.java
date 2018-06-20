package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.CompanyEntity;
import com.neituiquan.work.entity.CompanyImgEntity;
import com.neituiquan.work.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompanyController {


    @Autowired
    private CompanyService service;


    @RequestMapping(method = RequestMethod.POST,path = "/addCompanyImgList")
    public AbsEntity addCompanyImg(@RequestBody List<CompanyImgEntity> imgEntityList){
        return service.addCompanyImg(imgEntityList);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delCompanyImg")
    public AbsEntity delCompanyImg(@RequestParam String id){
        return service.delCompanyImg(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delCompanyAllImg")
    public AbsEntity delCompanyAllImg(@RequestParam String companyId){
        return service.delCompanyAllImg(companyId);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/bindCompany")
    public AbsEntity addCompany(@RequestBody CompanyEntity entity){
        return service.addCompany(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delCompany")
    public AbsEntity delCompany(@RequestParam String id){
        return service.delCompany(id);
    }


    @RequestMapping(method = RequestMethod.POST,path = "/updateCompany")
    public AbsEntity updateCompany(@RequestBody CompanyEntity entity){
        return service.updateCompany(entity);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/getCompany")
    public AbsEntity findCompanyById(@RequestParam String id){
        return service.findCompanyById(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getCompanyForUser")
    public AbsEntity findCompanyByUserId(@RequestParam String userId){
        return service.findCompanyByUserId(userId);
    }

}
