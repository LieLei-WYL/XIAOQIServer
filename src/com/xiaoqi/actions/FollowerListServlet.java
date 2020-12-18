package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.xiaoqi.entitys.AF;
import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.NoteInfo;
import com.xiaoqi.entitys.User;
import com.xiaoqi.services.NoteService;
import com.xiaoqi.services.UserService;
import com.xiaoqi.utils.DBUtil;

/**
 * 向客户端发送首页推荐笔记
 */
@WebServlet("/FollowerListServlet")
public class FollowerListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowerListServlet() {
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
		List<AF> afs = new ArrayList<AF>();
		List<String> followedPhones = new ArrayList<String>();
		sql = "select * from follow where followed_phone = '" + userPhone + "'";
		try {
			rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String followedPhone = rs.getString("follower_phone");
				followedPhones.add(followedPhone);
			}
			for (String string : followedPhones) {
				sql = "select * from users where phone = " + string;
				rs = dbUtil.queryDate(sql);
				while(rs.next()) {
					String phone = rs.getString("phone");
					String avatar = rs.getString("avatar");
					avatar = path + "/imgs/" + avatar;
					String name = rs.getString("name");
					String profile = rs.getString("profile");
					int followFlag;
					String sql1 = "select * from follow where follower_phone = '" + string + "' and followed_phone = '" + userPhone + "'";
					if(dbUtil.isExist(sql1)) {
						followFlag = 1;
					}else {
						followFlag = 0;
					}
					AF af = new AF(phone,avatar,name,profile,followFlag);
					afs.add(af);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		//把对象转换成JSON串
		String json = convertToJson(afs);
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
	private String convertToJson(List<AF> afs) {
		String json = null;
		//从内向外构造json串
		//创建JSONArray对象
		JSONArray jArray = new JSONArray();
		for (AF af : afs) {
			//每个user对象构造成一个JSONObject对象
			JSONObject jAF = new JSONObject();
			//向jUser中添加数据
			jAF.put("phone",af.getPhone());
			jAF.put("avatar",af.getAvatar());
			jAF.put("name",af.getName());
			jAF.put("profile",af.getProfile());
			jAF.put("followFlag",af.getFollowFlag());
			//把当前的JSONObject添加到JSONArray中
			jArray.put(jAF);
		}
		//创建外层JSONObject对象
		JSONObject jAFs = new JSONObject();
		jAFs.put("afs", jArray);
		json = jAFs.toString();
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
