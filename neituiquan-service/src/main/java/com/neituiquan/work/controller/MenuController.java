package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.MenuEntity;
import com.neituiquan.work.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MenuController {

    @Autowired
    private MenuService service;


    @RequestMapping(path = "/addMenu",method = RequestMethod.POST)
    public AbsEntity addMenu(@RequestBody MenuEntity entity){
        return service.addMenu(entity);
    }

    @RequestMapping(path = "/updateMenu",method = RequestMethod.POST)
    public AbsEntity updateMenu(@RequestBody MenuEntity entity){
        return service.updateMenu(entity);
    }

    @RequestMapping(path = "/delMenu",method = RequestMethod.GET)
    public AbsEntity delMenu(@RequestParam String id){
        return service.delMenu(id);
    }

    @RequestMapping(path = "/getAllMenu",method = RequestMethod.GET)
    public AbsEntity getAllMenu(){
        return service.getAllMenu();
    }
}
