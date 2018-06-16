package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.entity.UserEntity;
import com.neituiquan.work.service.UserService;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    private static List<String> imgFormat = new ArrayList<>();

    static{
        imgFormat.add(".png");
        imgFormat.add(".jpg");
        imgFormat.add(".jpeg");
        imgFormat.add(".bmp");
    }

    @RequestMapping(method = RequestMethod.POST,path = "/register")
    public AbsEntity register(UserEntity entity){
        return service.register(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/login")
    public AbsEntity login(UserEntity entity){
        return service.login(entity);
    }

    @RequestMapping(method = RequestMethod.POST,path = "/updateHeadImg")
    public AbsEntity updateHeadImg(@RequestParam("file") MultipartFile multipartFile,String id){
        AbsEntity absEntity = new AbsEntity();
        System.out.println(multipartFile.getOriginalFilename());
        String originalFilename = multipartFile.getOriginalFilename();
        String format = originalFilename.substring(originalFilename.lastIndexOf("."),originalFilename.length());
        if(!imgFormat.contains(format)){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "不支持的文件格式";
            return absEntity;
        }
        String fileName = StringUtils.getCurrentTimeMillis() + format;
        String filePath = FinalData.STATIC_FILE_PATH + fileName;
        if(multipartFile.isEmpty()){
            absEntity.code = FinalData.ERROR_CODE;
            absEntity.msg = "文件不能为空";
            return absEntity;
        }
        try {
            FileInputStream inputStream = (FileInputStream) multipartFile.getInputStream();
            byte[] bytes = new byte[1024];
            FileOutputStream outputStream = new FileOutputStream(filePath);
            int len;
            while ((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        service.updateHeadImg(id,fileName);
        absEntity.data = fileName;
        return absEntity;
    }


    @RequestMapping(method = RequestMethod.POST,value = "/updateUser")
    public AbsEntity updateUser(UserEntity entity){
        return service.updateUser(entity);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/updateLocation")
    public AbsEntity updateLocation(UserEntity entity){
        return service.updateLocation(entity);
    }

}