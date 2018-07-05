package com.uoocent.car.controller;

import com.uoocent.car.util.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.uoocent.car.entity.Attachment;
import com.uoocent.car.service.CarService;

@Controller
public class ImageController {

	@Resource
	private FileUtils fileUtils;
	
	/**
	 * Description: 获取图片信息，以流的形式返回
	 * @param file
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value="app/obd/get/image", method = RequestMethod.GET)
	public void load(String file, HttpServletResponse response, HttpServletRequest request) throws IOException {
		if (file.startsWith("static")) {
			file = request.getSession().getServletContext().getRealPath("/") + file;
		}
		ByteArrayOutputStream outputStream = null;
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		String imgFormat = FileUtils.getFileType(file);
		response.setContentType("image/" + imgFormat);
		File img = new File(file);
		if (!img.exists()) {
			file = request.getSession().getServletContext().getRealPath("/").concat("static/assets/img/default.jpg");
		}
		ServletOutputStream responseOutputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			BufferedImage image = ImageIO.read(new File(file));
			ImageIO.write(image, imgFormat, outputStream);
			responseOutputStream = response.getOutputStream();
			responseOutputStream.write(outputStream.toByteArray());
			responseOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(outputStream!=null){				
				outputStream.close();
			}
			if(responseOutputStream!=null){				
				responseOutputStream.close();
			}
		}
	}
	
	/**
	 * Description: 图片上传功能
	 * @param request
	 * @param tar
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "app/obd/upload/image", method = RequestMethod.POST)
	@ResponseBody
	public Attachment uploadImgae(HttpServletRequest request, String tar) throws Exception {
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		MultipartFile Filedata = req.getFile("file");
		Attachment attache = new Attachment();
		attache.setIsSuccess(false);
		try {
			if (Filedata == null || Filedata.isEmpty()) {
				attache.setMsg("文件为空");
				return attache;
			}
			if (StringUtils.isBlank(tar)||"null".equals(tar)) {
				tar = String.valueOf(System.nanoTime());
			}
			String name = Filedata.getOriginalFilename();
			String fileType = FileUtils.getFileType(name);
			String car_imag_path = fileUtils.uploadTmpFile(Filedata, tar);
			if (StringUtils.isBlank(fileType) || !("PNG".equals(fileType.toUpperCase()) || "JPG".equals(fileType.toUpperCase()) || "JPEG".equals(fileType.toUpperCase()))) {
				attache.setMsg("文件类型不合法");
				return attache;
			} else {
				attache.setTar(tar);
				attache.setName(name);
				attache.setExt(fileType);
				attache.setUrl(car_imag_path);
				attache.setIsSuccess(true);
			}

		} catch (Exception e) {
			attache.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return attache;
	}
	
}
