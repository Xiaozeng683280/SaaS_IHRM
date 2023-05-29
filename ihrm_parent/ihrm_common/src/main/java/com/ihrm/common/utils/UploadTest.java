package com.ihrm.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

import java.util.Date;


public class UploadTest {
    @Test
    public void testUploadImage() {
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "pOhLx7y-S_jFvCO14TsY1f9YCJ9lTwMFQvPw26ja";
        String secretKey = "YcZOIkh7ke8TqeFPi8K6koEbVL2aqXEaemM34Hgp";
        String bucket = "lilz";
        String localFilePath = "D:\\work\\SaaS-HRM--第16章.pdf";
        String prix = "http://rsvqyxm9o.hn-bkt.clouddn.com/";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "test";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(),
                    DefaultPutRet.class);

            System.err.println(response.bodyString());
            //返回请求地址
            System.out.println(prix + putRet.key + "?t=" + new Date().getTime());

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());

        }
    }
}
