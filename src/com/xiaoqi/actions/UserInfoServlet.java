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
 * ���տͻ��˵ıʼ�id��Ϣ(�ַ���)������ͻ��˷��ظñʼǵ�����ͼƬ��Ϣ�͵��������ղ��������������Լ����ۼ���
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
		in.close();
		if(!buffer.toString().equals("")) {
			userPhone = buffer.toString();
			System.out.println(userPhone);
			//ȡ��Application����  
	        ServletContext application = this.getServletContext();  
	        //����Application����  
	        application.setAttribute("userPhone",userPhone);
		}else {
			ServletContext application = this.getServletContext();
			userPhone = (String) application.getAttribute("userPhone");
		}
		
		//���û���������Ϣ
		String sql = "select * from users where phone = " + userPhone;
		System.out.println(sql);
		UserService userService = new UserService();
		List<User> users = userService.getUsers(sql);
		User user = users.get(0);
		
		//��ȡ��ǰ��վ���Ŀ¼
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
			//��ע����
			DBUtil dbUtil = new DBUtil();
			ResultSet rs;
			sql = "select count(*) from follow where follower_phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			attentionNum = rs.getString("count(*)");
			//��˿����
			sql = "select count(*) from follow where followed_phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			followerNum = rs.getString("count(*)");
			//�������ղ���
			List<String> userNotes = new ArrayList<String>();
			sql = "select * from notes where phone = " + userPhone;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String note = rs.getString("note_id");
				userNotes.add(note);
			}
			for (String string : userNotes) {
				//����
				sql = "select count(*) from likes where note_id = " + string;
				System.out.println(sql);
				rs = dbUtil.queryDate(sql);
				rs.next();
				num = num + rs.getInt("count(*)");
				//�ղ�
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
		
		//�Ѷ���ת����JSON��
		String json = convertToJson(user);
		System.out.println(json);
		//��JSON�����ظ��ͻ���
		out.write(json.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	/**
	 * �������Ķ���ת����Json��
	 * @param noteDetail
	 * @return json��
	 */
	private String convertToJson(User user) {
		String json = null;
		//ÿ��user�������һ��JSONObject����
		JSONObject jUser = new JSONObject();
		//��jUser���������
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
//			//�ñʼǵ�����ͼƬ��Ϣ
//			String sql = "select * from notes where note_id = " + noteId;
//			System.out.println(sql);
//			DBUtil dbUtil = new DBUtil();
//			ResultSet rs;
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			images = rs.getString("images");
//			//������
//			sql = "select count(*) from likes where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			likes = rs.getString("count(*)");
//			//�ղ���
//			sql = "select count(*) from collections where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			collections = rs.getString("count(*)");
//			//������
//			sql = "select count(*) from comments where note_id = " + noteId;
//			System.out.println(sql);
//			rs = dbUtil.queryDate(sql);
//			rs.next();
//			comments = rs.getString("count(*)");
//			
//			//���ۼ���
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
//		//�Ѷ���ת����JSON��
//		String json = convertToJson(noteDetail);
//		System.out.println(json);
//		//��JSON�����ظ��ͻ���
//		out.write(json.getBytes("UTF-8"));
//		out.flush();
//		out.close();
	}

}
