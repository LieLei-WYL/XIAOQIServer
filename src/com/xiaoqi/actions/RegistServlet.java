package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import com.xiaoqi.entitys.User;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ���տͻ��˵��û���Ϣ(�ַ���)������ͻ��˷��������֤���
 */
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
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
			String uName = buffer.toString().split(":")[0];
			String uPhone= buffer.toString().split(":")[1];
			String uPwd = buffer.toString().split(":")[2];
			
			//�����ݿ��ѯ���ݣ����ж��û��Ƿ���ڣ���ȡ�û���ݽ�������ؿͻ��ˣ�
			DBUtil dbUtil = new DBUtil();
			//�����û���Ϣƴ��sql���
			String sql = "select * from users where phone = '" + uPhone + "'";
			System.out.println(sql);
			boolean b = false;
			b = dbUtil.isExist(sql);
			if(b) {
				//�ֻ����ѱ�ע��
				out.write("EXIST".getBytes());
			}else {
				//�ֻ���δ��ע��
				User user = new User(uPhone,uName,uPwd);
				UserService userService = new UserService();
				if(userService.addUser(user)) {
					out.write("OK".getBytes());
				}else{
					out.write("ERROR".getBytes());
				}
			}
			out.flush();
			in.close();
			out.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
