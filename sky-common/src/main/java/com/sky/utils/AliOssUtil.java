package com.sky.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.sky.properties.AliOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


@Slf4j
@Component
public class AliOssUtil {
    @Autowired
    private AliOssProperties aliOssProperties;

    public String upload(byte[] content, String fileName) {
        String endpoint = aliOssProperties.getEndpoint();
        String bucketName = aliOssProperties.getBucketName();
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");

        if (accessKeyId == null || accessKeyId.isEmpty()) {
            accessKeyId = aliOssProperties.getAccessKeyId();
        }
        if (accessKeySecret == null || accessKeySecret.isEmpty()) {
            accessKeySecret = aliOssProperties.getAccessKeySecret();
        }

        String Dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String newFileName = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf("."));
        String objectname = Dir + '/' + newFileName;

        //创建实例
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        conf.setSignatureVersion(SignVersion.V2);

        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider, conf);

        //上传文件
        InputStream inputStream = new ByteArrayInputStream(content);
        ossClient.putObject(bucketName, objectname, inputStream);

        //关闭OSSClient
        ossClient.shutdown();
        
        //返回文件访问路径
        String url = "https://" + bucketName + "." + endpoint + "/" + objectname;
        log.info("上传路径:{}", url);
        return url;
    }

}