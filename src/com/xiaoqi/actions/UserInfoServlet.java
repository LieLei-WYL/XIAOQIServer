package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoqi.entitys.Comment;
import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteDetail;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.CommentService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

/**
 * 接收客户端的笔记id信息(字符串)，并向客户端返回该笔记的所有图片信息和点赞数、收藏数、评论数，以及评论集合
 */
@WebServlet("/UserInfoServlet")
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String userPhone;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if(!buffer.toString().equals("")) {
			userPhone = buffer.toString();
			System.out.println(userPhone);
			//取得Application对象  
	        ServletContext application = this.getServletContext();  
	        //设置Application属性  
	        application.setAttribute("userPhone",userPhone);
		}else {
			ServletContext application = this.getServletContext();
			userPhone = (String) application.getAttribute("userPhone");
		}
		
		//该用户的所有信息
		String sql = "select * from users where phone = " + userPhone;
		System.out.println(sql);
		UserService userService = new UserService();
		List<User> users = userService.getUsers(sql);
		User user = users.get(0);
		
		//获取当前的站点根目录
		String path = getServletContext().getContextPath();
//		System.out.println("path:" + path);
		if(user.getAvatar() != null) {
			String img = user.getAvatar();
			user.setAvatar(path + "/imgs/" + img);
		}
		if(user.getBackground() != null) {
			String img = user.getBackground();
			user.setBackground(path + "/imgs/" + img);
		}
		
		String attentionNum = "";
		String followerNum = "";
		String praisedNum = "";
		int num = 0;
		try {
			//关注人数
			DBUtil dbUtil = new DBUtil();
			ResultSet rs;
			sql = "select count(*) from follow where follower_phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			attentionNum = rs.getString("count(*)");
			//粉丝人数
			sql = "select count(*) from follow where followed_phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			followerNum = rs.getString("count(*)");
			//获赞与收藏数
			List<String> userNotes = new ArrayList<String>();
			sql = "select * from notes where phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String note = rs.getString("note_id");
				userNotes.add(note);
			}
			for (String string : userNotes) {
				//获赞
				sql = "select count(*) from likes where note_id = " + string;
				System.out.println(sql);
				rs = dbUtil.queryDate(sql);
				rs.next();
				num = num + rs.getInt("count(*)");
				//收藏
				sql = "select count(*) from collections where note_id = " + string;
				System.out.println(sql);
				rs = dbUtil.queryDate(sql);
				rs.next();
				num = num + rs.getInt("count(*)");
			}
			praisedNum = num + "";
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setAttentionNum(attentionNum);
		user.setFollowerNum(followerNum);
		user.setPraisedNum(praisedNum);
		
		//把对象转换成JSON串
		String json = convertToJson(user);
		System.out.println(json);
		//把JSON串返回给客户端
		out.write(json.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	/**
	 * 将给定的对象转换成Json串
	 * @param noteDetail
	 * @return json串
	 */
	private String convertToJson(User user) {
		String json = null;
		//每个user对象构造成一个JSONObject对象
		JSONObject jUser = new JSONObject();
		//向jUser中添加数据
		jUser.put("phone",user.getPhone());
		jUser.put("password",user.getPassword());
		jUser.put("avatar",user.getAvatar());
		jUser.put("name",user.getName());
		jUser.put("gender",user.getGender());
		jUser.put("birthday",user.getBirthday());
		jUser.put("area",user.getArea());
		jUser.put("profile",user.getProfile());
		jUser.put("background",user.getBackground());
		jUser.put("search",user.getSearch());
		jUser.put("attentionNum",user.getAttentionNum());
		jUser.put("followerNum",user.getFollowerNum());
		jUser.put("praisedNum",user.getPraisedNum());
		json = jUser.toString();
		return json;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

//		OutputStream out = response.getOutputStream();
//		String images = null;
//		String likes = null;
//		String collections = null;
//		String comments = null;
//		List<Comment> commentList = null;
//		try {
//			//该笔记的所有图片信息
//			String sql = "select * from notes where note_id = " + noteId;
//			System.out.println(sql);
//			DBUtil dbUtil = new DBUtil();
//			ResultSet rs;
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			images = rs.getString("images");
//			//点赞数
//			sql = "select count(*) from likes where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			likes = rs.getString("count(*)");
//			//收藏数
//			sql = "select count(*) from collections where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			collections = rs.getString("count(*)");
//			//评论数
//			sql = "select count(*) from comments where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			comments = rs.getString("count(*)");
//			
//			//评论集合
//			CommentService commentService = new CommentService();
//			sql = "select * from comments where note_id = " + noteId;
//			commentList = commentService.getComments(sql);
//			
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		NoteDetail noteDetail = new NoteDetail(images,likes,collections,comments,commentList);
//		//把对象转换成JSON串
//		String json = convertToJson(noteDetail);
//		System.out.println(json);
//		//把JSON串返回给客户端
//		out.write(json.getBytes("UTF-8"));
//		out.flush();
//		out.close();
	}

}
