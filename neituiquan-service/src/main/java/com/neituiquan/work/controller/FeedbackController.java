package com.neituiquan.work.controller;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.entity.FeedbackEntity;
import com.neituiquan.work.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService service;

    @RequestMapping(method = RequestMethod.POST,path = "/addFeedback")
    public AbsEntity addFeedback(@RequestBody FeedbackEntity entity){
        return service.addFeedback(entity);
    }

}
