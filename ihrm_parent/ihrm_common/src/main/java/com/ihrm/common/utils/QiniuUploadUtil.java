package com.ihrm.common.utils;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuUploadUtil {

    private static final String accessKey = "pOhLx7y-S_jFvCO14TsY1f9YCJ9lTwMFQvPw26ja";
    private static final String secretKey = "YcZOIkh7ke8TqeFPi8K6koEbVL2aqXEaemM34Hgp";
    private static final String bucket = "lilz";
    private static final String prix = "http://rsvqyxm9o.hn-bkt.clouddn.com/";
    private UploadManager manager;

    public QiniuUploadUtil() {
        //初始化基本配置
        Configuration cfg = new Configuration(Zone.zone2());
        //创建上传管理器
        manager = new UploadManager(cfg);
    }

	//文件名 = key
	//文件的byte数组
    public String upload(String imgName , byte [] bytes) {
        Auth auth = Auth.create(accessKey, secretKey);
        //构造覆盖上传token
        String upToken = auth.uploadToken(bucket,imgName);
        try {
            Response response = manager.put(bytes, imgName, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //返回请求地址
            return prix+putRet.key;
        } catch (Exception ex) {
            System.out.println( ex.getMessage());
        }
        return null;
    }
}
