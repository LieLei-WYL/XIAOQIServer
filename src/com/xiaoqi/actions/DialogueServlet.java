package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoqi.entitys.AF;
import com.xiaoqi.entitys.Dialogue;
import com.xiaoqi.entitys.DialogueMain;
import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.NoteService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

/**
 * ��ͻ��˷�����ҳ�Ƽ��ʼ�
 */
@WebServlet("/DialogueServlet")
public class DialogueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DialogueServlet() {
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
		String[] strs = buffer.toString().split(":");
		String targetPhone = strs[0];
		String currentPhone = strs[1];
		
		String path = getServletContext().getContextPath();
		
		//�������ڱ���ʼ����ݵļ���
//		List<Dialogue> sendList = new ArrayList<Dialogue>();
//		List<Dialogue> acceptList = new ArrayList<Dialogue>();
		List<Dialogue> dialogueList = new ArrayList<Dialogue>();
		try {
			sql = "select * from dialogueContent where sendPhone = '"+currentPhone+"' and acceptPhone = '"+targetPhone+"'";
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String sentence = rs.getString("sentence");
				String time = rs.getString("time");
				Dialogue dialogue = new Dialogue(id,sentence,time,0);
				dialogueList.add(dialogue);
			}
			sql = "select * from dialogueContent where sendPhone = '"+targetPhone+"' and acceptPhone = '"+currentPhone+"'";
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String sentence = rs.getString("sentence");
				String time = rs.getString("time");
				Dialogue dialogue = new Dialogue(id,sentence,time,1);
				dialogueList.add(dialogue);
			}
			
			Collections.sort(dialogueList, new Comparator<Dialogue>() {  
				@Override
				public int compare(Dialogue dialogue1, Dialogue dialogue2) {
					if (dialogue1.getId() > dialogue2.getId()) {  
	                    return 1;  
	                }else {
	                	return -1;
	                }
	            }
	        });  
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//�Ѷ���ת����JSON��
		String json = convertToJson(dialogueList);
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
	private String convertToJson(List<Dialogue> dialogueList) {
		String json = null;
		//�������⹹��json��
		//����JSONArray����
		JSONArray jArray = new JSONArray();
		for (Dialogue dialogue : dialogueList) {
			//ÿ��user�������һ��JSONObject����
			JSONObject jDialogue = new JSONObject();
			//��jUser���������
			jDialogue.put("sentence",dialogue.getSentence());
			jDialogue.put("time",dialogue.getTime());
			jDialogue.put("type",dialogue.getType());
			//�ѵ�ǰ��JSONObject��ӵ�JSONArray��
			jArray.put(jDialogue);
		}
		//�������JSONObject����
		JSONObject jDialogueList = new JSONObject();
		jDialogueList.put("dialogueList", jArray);
		json = jDialogueList.toString();
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

