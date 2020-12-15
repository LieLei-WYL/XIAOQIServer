package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaoqi.utils.DBUtil;

/**
 * 向客户端发送首页推荐笔记
 */
@WebServlet("/HotSearchServlet")
public class HotSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String phone;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HotSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		try {
			//通过流的方式接收客户端发来的字符串
			//获取网络输入流和输出流
			InputStream in = request.getInputStream();
//			OutputStream out = response.getOutputStream();
			//循环接收数据
			byte[] data = new byte[512];
			StringBuffer buffer = new StringBuffer();
			int length = -1;
			while((length = in.read(data, 0, data.length)) != -1) {
				buffer.append(new String(data, 0, length));
			}
			System.out.println(buffer.toString());
			phone = buffer.toString();
			
			DBUtil dbUtil = new DBUtil();
			ResultSet rs;
			String[] strs = new String[8];
			String sql = "SELECT * FROM hotsearch ORDER BY num desc limit 8";
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			int i = 0;
			while(rs.next()) {
				String str = rs.getString("search");
				strs[i] = str;
				i++;
				System.out.println(i+":"+str);
			}
			
			String hotSearches = String.join("、", strs);
			System.out.println("hotSearches:"+hotSearches);
			
			sql = "SELECT * FROM users where phone = '" + phone + "'";
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			String userSearch = rs.getString("search");
			System.out.println("userSearch:"+userSearch);
			
			String string = hotSearches + ":" + userSearch;
			
			//返回给客户端
			OutputStream out = response.getOutputStream();
			out.write(string.getBytes("UTF-8"));
			out.flush();
			out.close();
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
