package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import com.xiaoqi.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ���տͻ��˵��û���Ϣ(�ַ���)������ͻ��˷��������֤���
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//ͨ�����ķ�ʽ���տͻ��˷������ַ���
		//��ȡ�����������������
		InputStream in = request.getInputStream();
		OutputStream out = response.getOutputStream();
		//ѭ����������
		byte[] data = new byte[512];
		StringBuffer buffer = new StringBuffer();
		int length = -1;
		while((length = in.read(data, 0, data.length)) != -1) {
			buffer.append(new String(data, 0, length));
		}
		System.out.println(buffer.toString());
		String uPhone = buffer.toString().split(":")[0];
		String uPwd = buffer.toString().split(":")[1];
		
		//�����ݿ��ѯ���ݣ����ж��û��Ƿ���ڣ���ȡ�û���ݽ�������ؿͻ��ˣ�
		DBUtil dbUtil = new DBUtil();
		//�����û���Ϣƴ��sql���
		String sql1 = "select * from users where phone = '" + uPhone + "' and password = '" + uPwd +"'";
		System.out.println(sql1);
		boolean b = false;
		try {
			b = dbUtil.isExist(sql1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(b) {
			//�û���¼��Ϣ��ȷ
			out.write("YES".getBytes());
		}else {
			//�û���¼��Ϣ��ȷ
			out.write("NO".getBytes());
		}
		out.flush();
		in.close();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
