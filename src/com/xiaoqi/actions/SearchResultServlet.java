package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.NoteService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

/**
 * ��ͻ��˷�����ҳ�Ƽ��ʼ�
 */
@WebServlet("/SearchResultServlet")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchResultServlet() {
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

		DBUtil dbUtil = new DBUtil();
		ResultSet rs;
		String sql = "";
		//ͨ�����ķ�ʽ���տͻ��˷������ַ���
		//��ȡ�����������������
		InputStream in = request.getInputStream();
		OutputStream out = response.getOutputStream();
		//ѭ����������
		byte[] data = new byte[1024];
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while((temp = streamReader.readLine()) != null){
		    buffer.append(temp);
		}
		System.out.println(buffer.toString());
		String search = buffer.toString();
		
		//�����ر���ĵıʼ�ͼƬ�������ͻ���
		//�������
		NoteInfo noteInfo = new NoteInfo();
		//����������������(List<Note>)
		//��ȡ�ʼ���Ϣ
		NoteService noteService = new NoteService();
		sql = "select * from notes where phone in (select phone from users where name LIKE '%" + search + 
				"%') OR  title LIKE '%" + search + "%' OR content LIKE '%" + search + 
				"%' OR topic LIKE '%" + search + "%' OR area LIKE '%" + search + "%'";
		System.out.println(sql);
		//�������ڱ���ʼ����ݵļ���
		List<Note> notes = noteService.getNotes(sql);
		
		//TODO ��ȡ��ͷ���ڷ���˵�·��
		//��ȡ��ǰ��վ���Ŀ¼
		String path = getServletContext().getContextPath();
		System.out.println("path:" + path);
		
		//�������ڱ����û����ݵļ���
		UserService userService = new UserService();
		List<User> users = userService.getUsers("select * from users");
		
		//�򼯺�������ƴ����ͼƬ������
		for (Note note : notes) {
			String imgs[] = note.getImages().split("��");
			for(int i=0; i<imgs.length; i++) {
				imgs[i] = path + "/imgs/" + imgs[i];
			}
			String images = String.join("��",imgs);
			note.setImages(images);
			
			//��������ͷ��
			for(User user : users) {
				if(user.getPhone().equals(note.getAuthorPhone())) {
					note.setAuthorAvatar(path + "/imgs/" + user.getAvatar());
					note.setAuthorName(user.getName());
				}
			}
		}

		noteInfo.setNotes(notes);
		//�Ѷ���ת����JSON��
		String json = convertToJson(noteInfo);
		System.out.println(json);
		//��JSON�����ظ��ͻ���
//		OutputStream out = response.getOutputStream();
		out.write(json.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	/**
	 * �������Ķ���ת����Json��
	 * @param userInfo
	 * @return json��
	 */
	private String convertToJson(NoteInfo noteInfo) {
		String json = null;
		//�������⹹��json��
		//��ȡuserInfo�����еļ�������
		List<Note> notes = noteInfo.getNotes();
		//����JSONArray����
		JSONArray jArray = new JSONArray();
		for (Note note : notes) {
			//ÿ��user�������һ��JSONObject����
			JSONObject jNote = new JSONObject();
			//��jUser����������
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
			//�ѵ�ǰ��JSONObject���ӵ�JSONArray��
			jArray.put(jNote);
		}
		//�������JSONObject����
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