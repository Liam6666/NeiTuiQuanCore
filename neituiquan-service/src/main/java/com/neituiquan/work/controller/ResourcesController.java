package com.neituiquan.work.controller;


import com.neituiquan.work.base.AbsEntity;
import com.neituiquan.work.base.FinalData;
import com.neituiquan.work.utils.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 静态资源
 */
@RestController
public class ResourcesController {

    private static List<String> imgFormat = new ArrayList<>();

    static{
        imgFormat.add(".png");
        imgFormat.add(".jpg");
        imgFormat.add(".jpeg");
        imgFormat.add(".bmp");
    }

    @RequestMapping(path = "/img")
    public void img(HttpServletResponse response,@RequestParam String name)  throws IOException {
        String imagePath = FinalData.STATIC_FILE_PATH + name;
        try {
            FileInputStream fis = new FileInputStream(imagePath);
            int size = fis.available();
            byte data[] = new byte[size];
            fis.read(data);
            response.setContentType("image/jpeg"); //设置返回的文件类型
            OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
            fis.close();
        }catch (FileNotFoundException f){
            System.err.println(f.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.POST,path = "/uploadImg")
    public AbsEntity updateHeadImg(@RequestParam("file") MultipartFile multipartFile){
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
        absEntity.data = fileName;
        return absEntity;
    }
}
