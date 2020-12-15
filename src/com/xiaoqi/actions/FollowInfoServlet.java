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
 * 接收客户端的用户信息(字符串)，并向客户端返回身份验证结果
 */
@WebServlet("/FollowInfoServlet")
public class FollowInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowInfoServlet() {
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
			String currentUserPhone = buffer.toString().split(":")[0];
			String toUserPhone = buffer.toString().split(":")[1];
			
			//向数据库查询数据（先判断用户是否存在，获取用户身份结果，返回客户端）
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select * from follow where follower_phone = '" + currentUserPhone + "'and followed_phone = '" + toUserPhone + "'";
			System.out.println(sql);
			if(dbUtil.isExist(sql)) {
				out.write("YES".getBytes());
			}else {
				out.write("NO".getBytes());
			}
			out.flush();
			in.close();
			out.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
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
