package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaoqi.entitys.Comment;
import com.xiaoqi.services.CommentService;
import com.xiaoqi.utils.DBUtil;

/**
 * Servlet implementation class AddCommentServlet
 */
@WebServlet("/AddCommentServlet")
public class AddCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCommentServlet() {
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
			byte[] data = new byte[1024];
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
			StringBuffer buffer = new StringBuffer();
			String temp = null;
			while((temp = streamReader.readLine()) != null){
			    buffer.append(temp);
			}
			System.out.println(buffer.toString());
			
			String noteId = buffer.toString().split(":")[0];
			String phone = buffer.toString().split(":")[1];
			String content = buffer.toString().split(":")[2];
			String date = buffer.toString().split(":")[3];
			System.out.println(noteId+":"+phone+":"+content+":"+date);
			Comment comment = new Comment(Integer.parseInt(noteId),phone,content,date);
			CommentService commentService = new CommentService();
			commentService.addComment(comment);
			
			String sql = "SELECT max(comment_id) from comments";
			DBUtil dbUtil = new DBUtil();
			ResultSet rs = dbUtil.queryDate(sql);
			rs.next();
			String maxId = rs.getString("max(comment_id)");
			out.write(maxId.getBytes());
			out.flush();
			in.close();
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
