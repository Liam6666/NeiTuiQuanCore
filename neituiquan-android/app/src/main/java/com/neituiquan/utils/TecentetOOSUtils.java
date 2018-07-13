package com.neituiquan.utils;

import android.content.Context;

import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
/**
 * Created by Augustine on 2018/7/13.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 腾讯云 文件服务
 */

public class TecentetOOSUtils {

    private static String appid = "1256820169";
    private static String region = "ap-shanghai";
    private static String bucket = "neituiquan-1256820169";//存储桶名称(cos v5 的 bucket格式为：xxx-appid, 如 test-1253960454)

    private static String secretId = "AKIDEVkorqsNsCMLCsOo7HG9taHsJZgUEtTw";
    private static String secretKey ="Rt9E8wa5PYYOEvQmyzp4B7sSsCXovtjO";
    private static long keyDuration = 600; //SecretKey 的有效时间，单位秒

    private static String cosPath = "";//对象键，即存储到 COS 上的绝对路径

//    static String srcPath = "";//本地文件的绝对路径

    private static long signDuration = 600;//签名的有效期，单位为秒

    public static void upload(String basePath, String srcPath, CosXmlProgressListener progressListener, CosXmlResultListener resultListener){

        cosPath = basePath + System.currentTimeMillis() + getFileFormat(srcPath);

        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();

        LocalCredentialProvider localCredentialProvider = new LocalCredentialProvider(secretId, secretKey, keyDuration);

        CosXmlService cosXmlService = new CosXmlService(App.getAppInstance(),serviceConfig, localCredentialProvider);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        putObjectRequest.setSign(signDuration,null,null);

        /**
         * 上传进度回调
         */
        putObjectRequest.setProgressListener(progressListener);

        /**
         * 上传结果回调
         */
        cosXmlService.putObjectAsync(putObjectRequest, resultListener);

    }

    private static String getFileFormat(String fileAbsPath){
        return fileAbsPath.substring(fileAbsPath.lastIndexOf("."),fileAbsPath.length());
    }
}
