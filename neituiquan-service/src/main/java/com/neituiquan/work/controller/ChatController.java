package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.ChatHistoryEntity;
import com.neituiquan.work.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ChatController {

    @Autowired
    private ChatService service;

    @RequestMapping(method = RequestMethod.POST,path = "/sendChat")
    public AbsEntity addMsg(@RequestBody ChatHistoryEntity entity){
        return service.addMsg(entity);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/getChatList")
    public AbsEntity findMsgByReceiveId(@RequestParam String receiveId){
        return service.findMsgByReceiveId(receiveId);
    }

    @RequestMapping(method = RequestMethod.GET,path = "/updateChatState")
    public AbsEntity receiveMsg(@RequestParam String id){
        return service.receiveMsg(id);
    }
}
