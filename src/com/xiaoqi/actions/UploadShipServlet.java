package com.xiaoqi.actions;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import com.xiaoqi.utils.DBUtil;

/**
 * Servlet implementation class UploadShipServlet
 */
@WebServlet("/UploadShipServlet")
public class UploadShipServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String path;
 
	public UploadShipServlet() {
		super();
	}
 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		
		// 创建文件项目工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// 设置文件上传路径
		String uploadPath = this.getServletContext().getRealPath("/");
		
//		String uploadPath = getServletContext().getContextPath();
		uploadPath = uploadPath + "imgs\\";
		System.out.println("uploadPath:" + uploadPath);
		
		// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
		String temp = System.getProperty("java.io.tmpdir");
		// 设置缓冲区大小为 5M
		factory.setSizeThreshold(1024 * 1024 * 5);
		// 设置临时文件夹为temp
		factory.setRepository(new File(temp));
		// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
 
		// 解析结果放在List中
		try {
			List<FileItem> list = servletFileUpload.parseRequest(new ServletRequestContext(request));
 
			for (FileItem item : list) {
				String name = item.getFieldName();
				InputStream is = item.getInputStream();
 
				if (name.contains("content")) {
					System.out.println(inputStream2String(is));
				} else if (name.contains("img")) {
					try {
						path = uploadPath + item.getName();
						String avatar = item.getName();
						String phone = avatar.substring(0,11);

						System.out.println(avatar);
						System.out.println(phone);
						
						//向数据库修改数据
						DBUtil dbUtil = new DBUtil();
						//根据用户信息拼接sql语句
						String sql1 = "UPDATE users SET avatar = '"+avatar+"' WHERE phone='"+phone+"'";
						System.out.println(sql1);
						int b = 0;
						try {
							b = dbUtil.updateDataToTable(sql1);
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
						if(b > 0) {
							//用户信息修改成功
//							out.write("YES");
							System.out.println("头像信息修改成功");
						}else {
							//用户信息修改失败
//							out.write("NO");
							System.out.println("头像信息修改失败");
						}
						
						inputStream2File(is, path);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			out.write(path);  //这里我把服务端成功后，返回给客户端的是上传成功后路径
		} catch (FileUploadException e) {
			e.printStackTrace();
			System.out.println("failure");
			out.write("failure");
		}
 
		out.flush();
		out.close();
	}
 
	// 流转化成字符串
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
 
	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("文件保存路径为:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();
 
	}
}