package com.central.file.controller;

import java.util.Map;

import com.central.common.model.CommonResult;
import com.central.file.service.IFileService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.central.common.model.PageResult;
import com.central.file.model.FileInfo;

import javax.annotation.Resource;

/**
 * 文件上传
 *
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@RestController
public class FileController {
    @Resource
    private IFileService fileService;

    /**
     * 文件上传
     * 根据fileType选择上传方式
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/files-anon")
    public FileInfo upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fileService.upload(file);
    }

    /**
     * 文件删除
     *
     * @param id
     */
    @DeleteMapping("/files/{id}")
    public CommonResult delete(@PathVariable String id) {
        try {
            fileService.delete(id);
            return CommonResult.success();
        } catch (Exception ex) {
            return CommonResult.failed();
        }
    }

    /**
     * 文件查询
     *
     * @param params
     * @return
     */
    @GetMapping("/files")
    public PageResult<FileInfo> findFiles(@RequestParam Map<String, Object> params) {
        return fileService.findList(params);
    }
}
