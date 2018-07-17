package com.neituiquan.work.service;

import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.daoImpl.FeedbackDAOImpl;
import com.neituiquan.work.entity.FeedbackEntity;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackDAOImpl dao;

    public AbsEntity addFeedback(FeedbackEntity entity){
        entity.setId(StringUtils.getUUID());
        entity.setCreateTime(StringUtils.getCurrentTimeMillis());
        dao.addFeedback(entity);
        return new AbsEntity();
    }
}
