package com.sky.controller.common;


import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//通用接口
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class commonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);
        try {
            String fileName = file.getOriginalFilename();

            String url = aliOssUtil.upload(file.getBytes(), fileName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败",e);
        }
        return Result.error("文件上传失败");
    }
}
