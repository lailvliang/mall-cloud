package com.macro.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.macro.mall.dto.OssCallbackParam;
import com.macro.mall.dto.OssCallbackResult;
import com.macro.mall.service.OssService;
import com.macro.mall.common.FileUtil;
import com.macro.mall.common.IdentifierGenerator;
import com.macro.mall.dto.OssPolicyResult;
import com.macro.mall.exception.MallFileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * oss上传管理Service实现类
 * Created by macro on 2018/5/17.
 */
@Slf4j
@Service
public class OssServiceImpl implements OssService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);
	@Value("${aliyun.oss.policy.expire}")
	private int ALIYUN_OSS_EXPIRE;
	@Value("${aliyun.oss.maxSize}")
	private int ALIYUN_OSS_MAX_SIZE;
	@Value("${aliyun.oss.callback}")
	private String ALIYUN_OSS_CALLBACK;
	@Value("${aliyun.oss.bucketName}")
	private String ALIYUN_OSS_BUCKET_NAME;
	@Value("${aliyun.oss.endpoint}")
	private String ALIYUN_OSS_ENDPOINT;
	@Value("${aliyun.oss.dir.prefix}")
	private String ALIYUN_OSS_DIR_PREFIX;

	@Value("${aliyun.oss.accessKeyId}")
	private String ALIYUN_OSS_ACCESSKEYID;
	@Value("${aliyun.oss.accessKeySecret}")
	private String ALIYUN_OSS_ACCESSKEYSECRET;

	/**
	 * 本地临时存储目录
	 */
	@Value("${central.storage.tempdir}")
	private String MALL_STORAGE_TEMPDIR;

	@Autowired
	private OSSClient ossClient;

	/**
	 * 签名生成
	 */
	@Override
	public OssPolicyResult policy() {
		OssPolicyResult result = new OssPolicyResult();
		// 存储目录
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dir = ALIYUN_OSS_DIR_PREFIX+sdf.format(new Date());
		// 签名有效期
		long expireEndTime = System.currentTimeMillis() + ALIYUN_OSS_EXPIRE * 1000;
		Date expiration = new Date(expireEndTime);
		// 文件大小
		long maxSize = ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
		// 回调
		OssCallbackParam callback = new OssCallbackParam();
		callback.setCallbackUrl(ALIYUN_OSS_CALLBACK);
		callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
		callback.setCallbackBodyType("application/x-www-form-urlencoded");
		// 提交节点
		String action = "http://" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String policy = BinaryUtil.toBase64String(binaryData);
			String signature = ossClient.calculatePostSignature(postPolicy);
			String callbackData = BinaryUtil.toBase64String(JSONObject.toJSONString(callback).getBytes("utf-8"));
			// 返回结果
			result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
			result.setPolicy(policy);
			result.setSignature(signature);
			result.setDir(dir);
			result.setCallback(callbackData);
			result.setHost(action);
		} catch (Exception e) {
			LOGGER.error("签名生成失败", e);
		}
		return result;
	}

	@Override
	public OssCallbackResult callback(HttpServletRequest request) {
		OssCallbackResult result= new OssCallbackResult();
		String filename = request.getParameter("filename");
		filename = renameFilename(filename);
		log.info("新生成文件名：{}",filename);
		filename = "http://".concat(ALIYUN_OSS_BUCKET_NAME).concat(".").concat(ALIYUN_OSS_ENDPOINT).concat("/").concat(filename);
		result.setFilename(filename);
		result.setSize(request.getParameter("size"));
		result.setMimeType(request.getParameter("mimeType"));
		result.setWidth(request.getParameter("width"));
		result.setHeight(request.getParameter("height"));
		return result;
	}

	/**
	 * 重新命名 "uuid.格式"
	 * @param filename
	 * @return
	 */
	private String renameFilename(String filename){
		if(StringUtils.isEmpty(filename)){
			return filename;
		}
		//获取图片格式
		String[] strArr = filename.split("\\.");
		if(null !=strArr){
			int arrLen = strArr.length;
			String picformat = strArr[arrLen-1];
			StringBuilder sb = new StringBuilder(UUID.randomUUID().toString().replace("-","")).append(".").append(picformat);
			return sb.toString();
		}
		return "";
	}

//	public static void main(String[] args) {
//		OssServiceImpl t = new OssServiceImpl();
//		System.out.println(t.renameFilename("bb.cc.aaa.png"));
//	}

	/**
	 *
	 * @param inputStream
	 * @param suffix 文件后缀
	 * @return
	 */
	@Override
	public String uploadFileToAliyun(InputStream inputStream, String suffix) {
		// 生成随机的文件名
		String fileName = IdentifierGenerator.nextId() + suffix;
		//调用阿里云 oss把图片上传到 oss
		OSS ossClient = new OSSClientBuilder().build(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_ACCESSKEYID ,ALIYUN_OSS_ACCESSKEYSECRET);
		//上传
		PutObjectResult result = ossClient.putObject(ALIYUN_OSS_BUCKET_NAME , getStoragePath(fileName), inputStream);
		ossClient.shutdown();
		String picAddr = getFullEndpoint().concat("/").concat(getStoragePath(fileName));
//		String url = ossClient.generatePresignedUrl(ALIYUN_OSS_BUCKET_NAME ,fileName,new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10)).toString();
		return picAddr;
	}

	@Override
	public String upload(MultipartFile multipartFile, String suffix){
		String ossUploadResult = null;
		try {
			String fileName = IdentifierGenerator.nextId() + suffix;
			File tmpFile = FileUtil.getFile(MALL_STORAGE_TEMPDIR + fileName);///tmp/file/
			//存储本地
			multipartFile.transferTo(tmpFile);
			ossUploadResult = uploadFileToAliyun(new FileInputStream(tmpFile) ,suffix);
			if(Strings.isNotBlank(ossUploadResult)){
                //删除本地文件
                FileUtil.deleteFile(tmpFile);
            }
		} catch (Exception e) {
			e.printStackTrace();
			throw new MallFileUploadException(e.getMessage());
		}
		return ossUploadResult;
	}

	/**
	 * 阿里云存储地址 http://hzed-dev2.oss-cn-shenzhen.aliyuncs.com
	 * @return
	 */
	public String getFullEndpoint(){
		StringBuilder sb = new StringBuilder("http://").append(ALIYUN_OSS_BUCKET_NAME).append(".").append(ALIYUN_OSS_ENDPOINT);
		return sb.toString();
	}

	/**
	 * 存储路径 central/images/20191126/文件名
	 * @return
	 */
	public String getStoragePath(String fileName){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder(ALIYUN_OSS_DIR_PREFIX).append(sdf.format(new Date())).append("/").append(fileName);
		return sb.toString();
	}

	public boolean deleteFile(String filePath) {
		filePath = filePath.replaceAll(getFullEndpoint().concat("/"),"");
		OSS ossClient = new OSSClientBuilder().build(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_ACCESSKEYID ,ALIYUN_OSS_ACCESSKEYSECRET);
		boolean fileExist = ossClient.doesObjectExist(ALIYUN_OSS_BUCKET_NAME ,filePath);
		if (!fileExist) {
			log.warn("文件不存在 ,filePath:{}", filePath);
			return false;
		}
		ossClient.deleteObject(ALIYUN_OSS_BUCKET_NAME ,filePath);
		ossClient.shutdown();
		return true;
	}
}
