package com.itheima.qiniu;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;


/**
 * @author cong
 */
public class QiNiuTest {
    //使用七牛云提供的SDK实现将本地图片上传到七牛云服务器
    //@Test
    public void test1(){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
        String accessKey = "p6D92kSSmLDot_LphIBZqwh6ec5JfDzsifH2gmFi";
        String secretKey = "GpYHK7MTGbKZpvS_sOs7l1JyT2_ZA7aZnFx6fMgV";
        String bucket = "itcastspace";
    //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "E:\\qiniu\\test.jpg";
    //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
