package com.uoocent.car.util;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtils {

	private String ip;
	private String user;
	private String pwd;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * Description: 获取文件类型名
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		String name = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
		return name;

	}

	/**
	 * Description: 向FTP服务器上传文件
	 * @param tar FTP服务器保存目录
	 * @param file 输入流
	 * @return 成功返回true，否则返回false
	 */
	public String uploadTmpFile(MultipartFile file, String tar) {
		FTPClient ftp = new FTPClient();
		try{
			String name = file.getOriginalFilename();
			String fileType = getFileType(name);
			if (fileType != null) {
				if ("PNG".equals(fileType.toUpperCase()) || "JPG".equals(fileType.toUpperCase()) || "JPEG".equals(fileType.toUpperCase()) || "BMP".equals(fileType.toUpperCase())) {
					// 设置存放图片文件的路径
					String fileName = String.valueOf(System.nanoTime()) + String.valueOf(new Random().nextInt(1000)) + "." + fileType;
					String fileNameTemp = fileName + ".tmp";
					ftp.connect(getIp());//连接FTP服务器


					boolean reply = ftp.login(getUser(), getPwd());//登录
					if (!reply) {
						ftp.disconnect();
						return null;
					}
					ftp.enterLocalPassiveMode();
					ftp.changeWorkingDirectory("tmp");
					ftp.makeDirectory(tar);
					ftp.changeWorkingDirectory(tar);
					ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
					ftp.setControlEncoding("UTF-8");
					boolean isSuccess = ftp.storeFile(fileNameTemp, file.getInputStream());
					if(isSuccess){
						ftp.rename(fileNameTemp,fileName);
					}else{
						return  null;
					}
					ftp.logout();
					return "tmp/" + tar + "/" + fileName;
				} else {
					return null;
				}
			}
		}catch (Exception ex){
			ex.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return null;
	}
	/**
	 * Description: 向FTP服务器上传文件
	 * @param fromSrc FTP服务器临时保存目录
	 * @return 成功返回true，否则返回false
	 */
	public String uploadFile(String fromSrc) {
		FTPClient ftp = new FTPClient();
		try{

			int reply;
			ftp.connect(getIp(), 21);//连接FTP服务器
			ftp.login(getUser(), getPwd());//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return null;
			}
			String [] path = fromSrc.split("/");
			ftp.makeDirectory(path[1]);
			ftp.changeWorkingDirectory(path[0]);
			ftp.changeWorkingDirectory(path[1]);
			ftp.rename(path[2],"/" + path[1] + "/" + path[2]);
			if(ftp.listFiles("/" + path[0] + "/" + path[1]).length == 0) {
				ftp.removeDirectory("/" + path[0] + "/" + path[1]);
			}
			ftp.logout();
			return path[1] + "/" + path[2];
		}catch (Exception ex){
			ex.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return null;
	}
}
