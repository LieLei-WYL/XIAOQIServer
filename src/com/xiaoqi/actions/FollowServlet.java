package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaoqi.utils.DBUtil;

/**
 * Servlet implementation class FollowServlet
 */
@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowServlet() {
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
			in.close();
			String followFlag = buffer.toString().split(":")[0];
			String followerPhone = buffer.toString().split(":")[1];
			String followedPhone = buffer.toString().split(":")[2];
			System.out.println(followFlag+":"+followerPhone+":"+followedPhone);
			if(followFlag.equals("1")) {
				String sql = "insert into follow(follower_phone,followed_phone) values('"+ followerPhone +"','"+ followedPhone +"')";
				System.out.println(sql);
				DBUtil dbUtil = new DBUtil();
				int n = dbUtil.updateDataToTable(sql);
				if(n > 0) {
					System.out.println("数据修改成功");
				}
			}else if(followFlag.equals("0")){
				String sql = "DELETE FROM follow WHERE follower_phone='" + followerPhone + "' and followed_phone='" + followedPhone + "'";
				System.out.println(sql);
				DBUtil dbUtil = new DBUtil();
				int n;
				n = dbUtil.updateDataToTable(sql);
				if(n > 0) {
					System.out.println("数据删除成功");
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
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
