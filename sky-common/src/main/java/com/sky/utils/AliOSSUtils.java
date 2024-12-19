package com.sky.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.sky.properties.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

//把这个类交给spring管理
@Component
public class AliOSSUtils {

    @Autowired
    private AliOssProperties ali;

    public String upload(MultipartFile file) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 获取文件的原始名称和扩展名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // 创建OSS客户端
        OSS ossClient = new OSSClientBuilder()
                .build(ali.getEndpoint(), ali.getAccessKeyId(), ali.getAccessKeySecret());

        // 上传文件到OSS
        ossClient.putObject(ali.getBucketName(), fileName, inputStream);

        // 生成文件的URL
        String url = String.format("https://%s.%s/%s", ali.getBucketName(), ali.getEndpoint().replace("https://", ""), fileName);

        // 关闭OSS客户端
        ossClient.shutdown();

        return url;  // 返回文件的URL
    }


}
