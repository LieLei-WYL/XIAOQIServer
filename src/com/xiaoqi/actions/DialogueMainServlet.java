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
import com.xiaoqi.entitys.DialogueMain;
import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.NoteService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

/**
 * 向客户端发送首页推荐笔记
 */
@WebServlet("/DialogueMainServlet")
public class DialogueMainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DialogueMainServlet() {
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
		String userPhone = buffer.toString();
		
		String path = getServletContext().getContextPath();
		
		//创建用于保存笔记数据的集合
		List<DialogueMain> dialogueMains = new ArrayList<DialogueMain>();
		sql = "select * from dialogue where phone1 = '"+userPhone+"' or  phone2 = '"+userPhone+"'";
		try {
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String phone1 = rs.getString("phone1");
				String phone2 = rs.getString("phone2");
				String lastTime = rs.getString("lastTime");
				String lastSentence = rs.getString("lastSentence");
				DialogueMain dialogueMain;
				if(phone1.equals(userPhone)){
				    dialogueMain = new DialogueMain(phone2,lastTime,lastSentence);
				}else{
				    dialogueMain = new DialogueMain(phone1,lastTime,lastSentence);
				}
				dialogueMains.add(dialogueMain);
			}
			for (DialogueMain dialogueMain : dialogueMains) {
				sql = "select * from users where phone = " + dialogueMain.getPhone();
				rs = dbUtil.queryDate(sql);
				while(rs.next()) {
					String avatar = rs.getString("avatar");
					avatar = path + "/imgs/" + avatar;
					String name = rs.getString("name");
					dialogueMain.setAvatar(avatar);
					dialogueMain.setName(name);
				}
				sql = "select * from users where phone = " + userPhone;
				rs = dbUtil.queryDate(sql);
				while(rs.next()) {
					String currentAvatar = rs.getString("avatar");
					currentAvatar = path + "/imgs/" + currentAvatar;
					dialogueMain.setCurrentAvatar(currentAvatar);
				}
			}
			
			Collections.sort(dialogueMains, new Comparator<DialogueMain>() {  
				@Override
				public int compare(DialogueMain dialogueMain1, DialogueMain dialogueMain2) {
					//格式化时间
			        SimpleDateFormat CurrentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        String date1=dialogueMain1.getLastTime();
			        String date2=dialogueMain2.getLastTime();
			        try {
			            Date dateTime1 = CurrentTime.parse(date1);
			            Date dateTime2 = CurrentTime.parse(date2);
			            if (dateTime1.getTime() > dateTime2.getTime()) {  
		                    return -1;  
		                }  
		                if (dateTime1.getTime() == dateTime2.getTime()) {  
		                    return 0;  
		                }
			 
			        } catch (ParseException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			        }
			        return 1;  
	            }
	        });  
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//把对象转换成JSON串
		String json = convertToJson(dialogueMains);
		System.out.println(json);
		//把JSON串返回给客户端
//		OutputStream out = response.getOutputStream();
		out.write(json.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	/**
	 * 将给定的对象转换成Json串
	 * @param userInfo
	 * @return json串
	 */
	private String convertToJson(List<DialogueMain> dialogueMains) {
		String json = null;
		//从内向外构造json串
		//创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (DialogueMain dialogueMain : dialogueMains) {
			//每个user对象构造成一个JSONObject对象
			JSONObject jDialogueMain = new JSONObject();
			//向jUser中添加数据
			jDialogueMain.put("phone",dialogueMain.getPhone());
			jDialogueMain.put("avatar",dialogueMain.getAvatar());
			jDialogueMain.put("currentAvatar",dialogueMain.getCurrentAvatar());
			jDialogueMain.put("name",dialogueMain.getName());
			jDialogueMain.put("lastTime",dialogueMain.getLastTime());
			jDialogueMain.put("lastSentence",dialogueMain.getLastSentence());
			//把当前的JSONObject添加到JSONArray中
			jArray.put(jDialogueMain);
		}
		//创建外层JSONObject对象
		JSONObject jDialogueMains = new JSONObject();
		jDialogueMains.put("dialogueMains", jArray);
		json = jDialogueMains.toString();
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
