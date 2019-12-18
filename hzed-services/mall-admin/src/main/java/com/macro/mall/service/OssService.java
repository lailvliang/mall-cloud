package com.macro.mall.service;

import com.macro.mall.dto.OssCallbackResult;
import com.macro.mall.dto.OssPolicyResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * oss上传管理Service
 * Created by macro on 2018/5/17.
 */
public interface OssService {
    /**
     * oss上传策略生成
     */
    OssPolicyResult policy();
    /**
     * oss上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);

    /**
     * 上传文件到阿里云
     * @param inputStream
     * @param suffix
     * @return
     */
    String uploadFileToAliyun(InputStream inputStream, String suffix);

    /**
     * 上传文件
     * @param multipartFile
     * @param suffix 文件后缀，如:.jpg
     * @return
     */
    String upload(MultipartFile multipartFile, String suffix);
}
