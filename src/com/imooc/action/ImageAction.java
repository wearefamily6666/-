package com.imooc.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;

import com.mchange.io.FileUtils;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 访问图片action
 * @author Administrator
 *
 */
public class ImageAction extends ActionSupport {
	private String userHeader;
	private String groupHeader;
 
 
	public void setUserHeader(String userHeader) {
		this.userHeader = userHeader;
	}

	public void setGroupHeader(String groupHeader) {
		this.groupHeader = groupHeader;
	}

	//获取输出对象
	public static ServletOutputStream getInstance() throws IOException {
		HttpServletResponse response=ServletActionContext.getResponse(); 
		response.setCharacterEncoding("utf-8");
		ServletOutputStream servletOutputStream=response.getOutputStream();
		 return servletOutputStream;
	}

	//访问图片
	@Test
	public void imageInteface()   {
		 try {
				//图片url格式：http://localhost:8080/The_vertical_and_horizontal/image_imageInteface?groupHeader=张三_1.jpg
			 String subString=""; 
			 if (userHeader!=null) {
				 subString= "userHeader/"+userHeader;
			}else if(groupHeader!=null){
				 subString= "groupHeader/"+groupHeader;
			}
			 System.out.println(subString);
				 File file=new File("e://天下纵横/"+subString);
				 FileInputStream fileInputStream=new FileInputStream(file);
				 ByteArrayOutputStream baos=new ByteArrayOutputStream();
				 int len=0;
				 byte buffer[]=new byte[1024];
				 while((len=fileInputStream.read(buffer))>0) {
					 baos.write(buffer);
				 }
				 getInstance().write(baos.toByteArray());
				 if (baos!=null) {
					baos.close();
				}
				 if (fileInputStream!=null) {
					fileInputStream.close();
				}
				 getInstance().close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
	}
	//处理上传文件
	//type :1 用户头像 2：群头像
	public static void doUpLoad(int type, File file, String str) throws Exception {
		String pathname="e://天下纵横/";
		if (type==1) {
			pathname+="userHeader/"+str;
		}else {
			pathname+="groupHeader/"+str;
		} 
		File destFile=new File(pathname);
		org.apache.commons.io.FileUtils.copyFile(file, destFile);

	}
}
