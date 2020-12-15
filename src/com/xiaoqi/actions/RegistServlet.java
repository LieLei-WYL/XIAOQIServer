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
 * 接收客户端的用户信息(字符串)，并向客户端返回身份验证结果
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
			//通过流的方式接收客户端发来的字符串
			//获取网络输入流和输出流
			InputStream in = request.getInputStream();
			OutputStream out = response.getOutputStream();
			//循环接收数据
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
			
			//向数据库查询数据（先判断用户是否存在，获取用户身份结果，返回客户端）
			DBUtil dbUtil = new DBUtil();
			//根据用户信息拼接sql语句
			String sql = "select * from users where phone = '" + uPhone + "'";
			System.out.println(sql);
			boolean b = false;
			b = dbUtil.isExist(sql);
			if(b) {
				//手机号已被注册
				out.write("EXIST".getBytes());
			}else {
				//手机号未被注册
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
