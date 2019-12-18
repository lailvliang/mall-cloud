package com.macro.mall.common;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author dsy on 2019/11/26
 */
@Slf4j
public class FileUtil {
    public static File getFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return file;
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file){
        if(Objects.nonNull(file) && file.isFile() && file.exists()){
            return file.delete();
        }
        return false;
    }
    public String getFullEndpoint(String bucketName ,String endpoint){
        File file = new File("");
        return null;
    }

    public static String getFilenameSuffix(String filename){
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        return new StringBuilder(".").append(suffix).toString();
    };
}
