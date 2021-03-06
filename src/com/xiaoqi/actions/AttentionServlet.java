package com.xiaoqi.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.NoteService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 接收客户端的用户信息(字符串)，并向客户端返回身份验证结果
 */
@WebServlet("/AttentionServlet")
public class AttentionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttentionServlet() {
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
//			OutputStream out = response.getOutputStream();
			//循环接收数据
			byte[] data = new byte[512];
			StringBuffer buffer = new StringBuffer();
			int length = -1;
			while((length = in.read(data, 0, data.length)) != -1) {
				buffer.append(new String(data, 0, length));
			}
			String phone = buffer.toString();
			
			String sql = "select * from follow where follower_phone = '" + phone + "'";
			List<String> followedPhones = new ArrayList<String>();
			DBUtil dbUtil = new DBUtil();
			ResultSet rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String followedPhone = rs.getString("followed_phone");
				followedPhones.add(followedPhone);
			}
			
			//将本地保存的的笔记图片发送至客户端
			//构造对象
			NoteInfo noteInfo = new NoteInfo();
			//往对象中添加数据(List<Note>)
			//获取笔记信息
			NoteService noteService = new NoteService();
			//创建用于保存笔记数据的集合
			List<Note> notes = new ArrayList<Note>();
			List<Note> noteList = new ArrayList<Note>();
			for (String string : followedPhones) {
				noteList.clear();
				noteList = noteService.getNotes("select * from notes where phone='" + string + "'");
				System.out.println("noteList::"+noteList.toString());
				for (Note note : noteList) {
					notes.add(note);
				}
			}
			//TODO 获取到头像在服务端的路径
			//获取当前的站点根目录
			String path = getServletContext().getContextPath();
			System.out.println("path:" + path);
			
			//创建用于保存用户数据的集合
			UserService userService = new UserService();
			List<User> users = userService.getUsers("select * from users");
			
			//向集合中添加拼接上图片的名称
			for (Note note : notes) {
				String imgs[] = note.getImages().split("、");
				for(int i=0; i<imgs.length; i++) {
					imgs[i] = path + "/imgs/" + imgs[i];
				}
				String images = String.join("、",imgs);
				note.setImages(images);
				
				//加入作者头像
				for(User user : users) {
					if(user.getPhone().equals(note.getAuthorPhone())) {
						note.setAuthorAvatar(path + "/imgs/" + user.getAvatar());
						note.setAuthorName(user.getName());
					}
				}
			}

			noteInfo.setNotes(notes);
			//把对象转换成JSON串
			String json = convertToJson(noteInfo);
			System.out.println(json);
			//把JSON串返回给客户端
			OutputStream out = response.getOutputStream();
			out.write(json.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 将给定的对象转换成Json串
	 * @param userInfo
	 * @return json串
	 */
	private String convertToJson(NoteInfo noteInfo) {
		String json = null;
		//从内向外构造json串
		//获取userInfo对象中的集合属性
		List<Note> notes = noteInfo.getNotes();
		//创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (Note note : notes) {
			//每个user对象构造成一个JSONObject对象
			JSONObject jNote = new JSONObject();
			//向jUser中添加数据
			jNote.put("note_id",note.getNoteId());
			jNote.put("phone",note.getAuthorPhone());
			jNote.put("avatar",note.getAuthorAvatar());
			jNote.put("name",note.getAuthorName());
			jNote.put("images",note.getImages());
			jNote.put("title",note.getTitle());
			jNote.put("content",note.getContent());
			jNote.put("topic",note.getTopic());
			jNote.put("date",note.getDate());
			jNote.put("area",note.getArea());
			//把当前的JSONObject添加到JSONArray中
			jArray.put(jNote);
		}
		//创建外层JSONObject对象
		JSONObject jNotes = new JSONObject();
		jNotes.put("notes", jArray);
		json = jNotes.toString();
		return json;
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
