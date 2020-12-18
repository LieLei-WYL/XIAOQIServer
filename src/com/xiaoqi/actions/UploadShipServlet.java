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
		
		// �����ļ���Ŀ��������
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// �����ļ��ϴ�·��
		String uploadPath = this.getServletContext().getRealPath("/");
		
//		String uploadPath = getServletContext().getContextPath();
		uploadPath = uploadPath + "imgs\\";
		System.out.println("uploadPath:" + uploadPath);
		
		// ��ȡϵͳĬ�ϵ���ʱ�ļ�����·������·��ΪTomcat��Ŀ¼�µ�temp�ļ���
		String temp = System.getProperty("java.io.tmpdir");
		// ���û�������СΪ 5M
		factory.setSizeThreshold(1024 * 1024 * 5);
		// ������ʱ�ļ���Ϊtemp
		factory.setRepository(new File(temp));
		// �ù���ʵ�����ϴ����,ServletFileUpload ���������ļ��ϴ�����
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
 
		// �����������List��
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
						
						//�����ݿ��޸�����
						DBUtil dbUtil = new DBUtil();
						//�����û���Ϣƴ��sql���
						String sql1 = "UPDATE users SET avatar = '"+avatar+"' WHERE phone='"+phone+"'";
						System.out.println(sql1);
						int b = 0;
						try {
							b = dbUtil.updateDataToTable(sql1);
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
						if(b > 0) {
							//�û���Ϣ�޸ĳɹ�
//							out.write("YES");
							System.out.println("ͷ����Ϣ�޸ĳɹ�");
						}else {
							//�û���Ϣ�޸�ʧ��
//							out.write("NO");
							System.out.println("ͷ����Ϣ�޸�ʧ��");
						}
						
						inputStream2File(is, path);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			out.write(path);  //�����Ұѷ���˳ɹ��󣬷��ظ��ͻ��˵����ϴ��ɹ���·��
		} catch (FileUploadException e) {
			e.printStackTrace();
			System.out.println("failure");
			out.write("failure");
		}
 
		out.flush();
		out.close();
	}
 
	// ��ת�����ַ���
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
 
	// ��ת�����ļ�
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		System.out.println("�ļ�����·��Ϊ:" + savePath);
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