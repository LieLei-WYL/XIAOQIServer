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
 * 向客户端发送首页推荐笔记
 */
@WebServlet("/AddDialogueServlet")
public class AddDialogueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDialogueServlet() {
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
		String[] strs = buffer.toString().split(";");
		String currentPhone = strs[0];
		String targetPhone = strs[1];
		String sentence = strs[2];
		String time = strs[3];
		
		String path = getServletContext().getContextPath();
		
		//创建用于保存笔记数据的集合
//		List<Dialogue> sendList = new ArrayList<Dialogue>();
//		List<Dialogue> acceptList = new ArrayList<Dialogue>();
		List<Dialogue> dialogueList = new ArrayList<Dialogue>();
		try {
			sql = "select * from dialogue where phone1 = '"+currentPhone+"' and phone2 = '"+targetPhone+"'";
			System.out.println(sql);
			if(dbUtil.isExist(sql)) {
				sql = "UPDATE dialogue SET lastTime = '"+time+"',lastSentence = '"+sentence+"' WHERE phone1 = '"+currentPhone+"' and phone2 = '"+targetPhone+"'";
				System.out.println(sql);
				if(dbUtil.updateDataToTable(sql) > 0) {
					out.write("OK".getBytes());
				}else {
					out.write("ERROR".getBytes());
				}
			}else {
				sql = "select * from dialogue where phone1 = '"+targetPhone+"' and phone2 = '"+currentPhone+"'";
				System.out.println(sql);
				if(dbUtil.isExist(sql)) {
					sql = "UPDATE dialogue SET lastTime = '"+time+"',lastSentence = '"+sentence+"' WHERE phone1 = '"+targetPhone+"' and phone2 = '"+currentPhone+"'";
					System.out.println(sql);
					if(dbUtil.updateDataToTable(sql) > 0) {
						out.write("OK".getBytes());
					}else {
						out.write("ERROR".getBytes());
					}
				}else {
					sql = "insert into dialogue(phone1,phone2,lastTime,lastSentence) values('"+currentPhone+"','"+targetPhone+"','"+time+"','"+sentence+"')";
					System.out.println(sql);
					if(dbUtil.updateDataToTable(sql) > 0) {
						out.write("OK".getBytes());
					}else {
						out.write("ERROR".getBytes());
					}
				}
			}
			sql = "insert into dialogueContent(sendPhone,acceptPhone,sentence,time) values('"+currentPhone+"','"+targetPhone+"','"+sentence+"','"+time+"')";
			System.out.println(sql);
			if(dbUtil.updateDataToTable(sql) > 0) {
				out.write("OK".getBytes());
			}else {
				out.write("ERROR".getBytes());
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//把对象转换成JSON串
//		String json = convertToJson(dialogueList);
//		System.out.println(json);
		//把JSON串返回给客户端
//		OutputStream out = response.getOutputStream();
//		out.write(json.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}

