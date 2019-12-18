package com.macro.mall.controller;


import com.macro.mall.dto.OssCallbackResult;
import com.macro.mall.service.impl.OssServiceImpl;
import com.macro.mall.common.CommonResult;
import com.macro.mall.common.FileUtil;
import com.macro.mall.dto.OssPolicyResult;
import com.macro.mall.dto.resp.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Oss相关操作接口
 * Created by macro on 2018/4/26.
 */
@Slf4j
@RestController
@Api(tags = "OssController", description = "Oss管理")
@RequestMapping("/aliyun/oss")
public class OssController {
    @Autowired
    private OssServiceImpl ossService;

    @ApiOperation(value = "oss上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @RequestMapping(value = "callback", method = RequestMethod.POST)
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return CommonResult.success(ossCallbackResult);
    }

    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public CommonResult<String> uploadFile(@RequestParam("file") MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        log.info("正在做上传操作，上传文件为：{}",originalFilename);
        //简单文件名判断，存在小概率的不准确
        String resultFileUrl = ossService.upload(file, FileUtil.getFilenameSuffix(originalFilename));
        return CommonResult.success(resultFileUrl);
    }

    @ApiOperation(value = "删除图片")
    @RequestMapping(value = "deleteFile", method = RequestMethod.GET)
    public CommonResult<String> deleteFile(@RequestParam(value = "fileAddr",required = true ) String fileAddr){
        boolean deleteResult = false;
        try {
            log.info("正在做删除操作，删除文件为：{}",fileAddr);
            deleteResult = ossService.deleteFile(fileAddr);
        } catch (Exception e) {
            e.printStackTrace();
            Response.getFailResponse("删除图片异常："+e.getMessage());
        }
        return deleteResult ?CommonResult.success(fileAddr):CommonResult.failed("删除失败");
    }
}
