package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.MsgTaskEntity;
import com.neituiquan.work.service.MsgTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class MsgTaskController {

    @Autowired
    private MsgTaskService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addMsgTask")
    public AbsEntity addMsgTask(@RequestBody MsgTaskEntity entity){
        return service.addMsgTask(entity);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/findMsgTaskById")
    public AbsEntity findMsgTaskById(@RequestParam String id){
        return service.findMsgTaskById(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getMsgTaskList")
    public AbsEntity getMsgTaskList(@RequestParam String receiveId){
        return service.getMsgTaskList(receiveId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delMsgTaskById")
    public AbsEntity delMsgTaskById(@RequestParam String id){
        return service.delMsgTaskById(id);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/delMsgTaskAdd2History")
    public AbsEntity delMsgTaskAdd2History(String id){
        return service.delMsgTaskAdd2History(id);
    }
}
