package com.neituiquan.work.controller;


import com.neituiquan.work.base.FinalData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 静态资源
 */
@RestController
public class ResourcesController {

    @RequestMapping(path = "/img")
    public void img(HttpServletResponse response,@RequestParam String name)  throws IOException {
        String imagePath = FinalData.STATIC_FILE_PATH + name;
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
    }
}
