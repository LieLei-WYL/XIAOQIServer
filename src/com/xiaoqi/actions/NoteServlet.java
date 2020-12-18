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
@WebServlet("/NoteServlet")
public class NoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String noteId;
	private String followerPhone;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoteServlet() {
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
			noteId = buffer.toString().split(":")[0];
			followerPhone = buffer.toString().split(":")[1];
			System.out.println(noteId+":"+followerPhone);
//			if(followerPhone == null) {
//				
//			}
			//ȡ��Application����  
	        ServletContext application = this.getServletContext();  
	        //����Application����  
	        application.setAttribute("noteId",noteId);
	        application.setAttribute("followerPhone",followerPhone);
		}else {
			ServletContext application = this.getServletContext();
			noteId = (String) application.getAttribute("noteId");
			followerPhone = (String) application.getAttribute("followerPhone");
		}
		
		String followedPhone = null;
		
		String avatar = null;
		String name = null;
		
		int followFlag = 0;
		int likeFlag = 0;
		int collectionFlag = 0;
		String images = null;
		String likes = null;
		String collections = null;
		String comments = null;
		List<Comment> commentList = null;
		try {
			//�ñʼǵ�����ͼƬ��Ϣ
			String sql = "select * from notes where note_id = " + noteId;
			System.out.println(sql);
			DBUtil dbUtil = new DBUtil();
			ResultSet rs;
			rs = dbUtil.queryDate(sql);
			rs.next();
			followedPhone = rs.getString("phone");
			images = rs.getString("images");
			
			if(followerPhone != null) {
				sql = "select * from users where phone = '" + followerPhone + "'";
				rs = dbUtil.queryDate(sql);
				rs.next();
				avatar = rs.getString("avatar");
				name = rs.getString("name");
			}
			
			//��ȡ��ǰ��վ���Ŀ¼
			String path = getServletContext().getContextPath();
			System.out.println("path:" + path);
			String[] imgs = images.split("��");
			for(int i=0; i<imgs.length; i++) {
				imgs[i] = path + "/imgs/" + imgs[i];
			}
			images = String.join("��",imgs);
			
			avatar = path + "/imgs/" + avatar;
		
			//�Ƿ��ע
			sql = "select * from follow where follower_phone = '" + followerPhone + "'and followed_phone = '" + followedPhone + "'";
			System.out.println(sql);
			if(dbUtil.isExist(sql)) {
				followFlag = 1;
			}
			//�Ƿ����
			sql = "select * from likes where phone = '" + followerPhone + "'and note_id = '" + noteId + "'";
			System.out.println(sql);
			if(dbUtil.isExist(sql)) {
				likeFlag = 1;
			}
			//�Ƿ��ղ�
			sql = "select * from collections where phone = '" + followerPhone + "'and note_id = '" + noteId + "'";
			System.out.println(sql);
			if(dbUtil.isExist(sql)) {
				collectionFlag = 1;
			}
			//������
			sql = "select count(*) from likes where note_id = " + noteId;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			likes = rs.getString("count(*)");
			//�ղ���
			sql = "select count(*) from collections where note_id = " + noteId;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			collections = rs.getString("count(*)");
			//������
			sql = "select count(*) from comments where note_id = " + noteId;
			System.out.println(sql);
			rs = dbUtil.queryDate(sql);
			rs.next();
			comments = rs.getString("count(*)");
			
			//���ۼ���
			CommentService commentService = new CommentService();
			sql = "select * from comments where note_id = " + noteId;
			commentList = commentService.getComments(sql);
			
			//�������ڱ����û����ݵļ���
			UserService userService = new UserService();
			List<User> users = userService.getUsers("select * from users");
			for(int i=0;i<commentList.size();i++) {
				//��������ͷ��
				for(User user : users) {
					if(user.getPhone().equals(commentList.get(i).getPhone())) {
						commentList.get(i).setAvatar(path + "/imgs/" + user.getAvatar());
						commentList.get(i).setName(user.getName());
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NoteDetail noteDetail = new NoteDetail(avatar,name,images,followFlag,likeFlag,collectionFlag,likes,collections,comments,commentList);
		//�Ѷ���ת����JSON��
		String json = convertToJson(noteDetail);
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
	private String convertToJson(NoteDetail noteDetail) {
		String json = null;
		//�������⹹��json��
		//��ȡnoteDetail�����еļ�������
		List<Comment> commentList = noteDetail.getCommentList();
		//����JSONArray����
		JSONArray jArray = new JSONArray();
		for (Comment comment : commentList) {
			//ÿ��user�������һ��JSONObject����
			JSONObject jComment = new JSONObject();
			//��jUser���������
			jComment.put("comment_id",comment.getCommentId());
			jComment.put("note_id",comment.getNoteId());
			jComment.put("phone",comment.getPhone());
			jComment.put("avatar",comment.getAvatar());
			jComment.put("name",comment.getName());
			jComment.put("content",comment.getContent());
			jComment.put("date",comment.getDate());
			jComment.put("likes",comment.getLikes());
			//�ѵ�ǰ��JSONObject��ӵ�JSONArray��
			jArray.put(jComment);
		}
		
		JSONObject jAvatar = new JSONObject();
		jAvatar.put("avatar",noteDetail.getAvatar());
		JSONObject jName = new JSONObject();
		jName.put("name",noteDetail.getName());
		
		JSONObject jImages = new JSONObject();
		jImages.put("images",noteDetail.getImages());
		JSONObject jFollowFlag = new JSONObject();
		jFollowFlag.put("followFlag",noteDetail.getFollowFlag());
		JSONObject jLikeFlag = new JSONObject();
		jLikeFlag.put("likeFlag",noteDetail.getLikeFlag());
		JSONObject jCollectionFlag = new JSONObject();
		jCollectionFlag.put("collectionFlag",noteDetail.getCollectionFlag());
		JSONObject jLikes = new JSONObject();
		jLikes.put("likes",noteDetail.getLikes());
		JSONObject jCollections = new JSONObject();
		jCollections.put("collections",noteDetail.getCollections());
		JSONObject jComments = new JSONObject();
		jComments.put("comments",noteDetail.getComments());
		//�������JSONObject����
		JSONObject jCommentList = new JSONObject();
		jCommentList.put("avatar", jAvatar);
		jCommentList.put("name", jName);
		jCommentList.put("images", jImages);
		jCommentList.put("followFlag", jFollowFlag);
		jCommentList.put("likeFlag", jLikeFlag);
		jCommentList.put("collectionFlag", jCollectionFlag);
		jCommentList.put("likes", jLikes);
		jCommentList.put("collections", jCollections);
		jCommentList.put("comments", jComments);
		jCommentList.put("commentList", jArray);
		json = jCommentList.toString();
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
