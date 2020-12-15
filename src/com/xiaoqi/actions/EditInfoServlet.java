package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;

import com.xiaoqi.utils.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接收客户端的用户信息(字符串)，并向客户端返回身份验证结果
 */
@WebServlet("/EditInfoServlet")
public class EditInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditInfoServlet() {
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
		byte[] data = new byte[1024];
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(in,"utf-8"));
		StringBuffer buffer = new StringBuffer();
		String temp = null;
		while((temp = streamReader.readLine()) != null){
		    buffer.append(temp);
		}
		System.out.println(buffer.toString());
		
//		avatar1+":"+name1+":"+phone1+":"+gender1+":"+birthday1+":"+area1+":"+intro1+":"+bg1+":"+newPwd;
		
		String avatar1 = buffer.toString().split(":")[0];
		String name = buffer.toString().split(":")[1];
		String phone = buffer.toString().split(":")[2];
		String gender = buffer.toString().split(":")[3];
		String birthday = buffer.toString().split(":")[4];
		String area = buffer.toString().split(":")[5];
		String intro = buffer.toString().split(":")[6];
		String bg1 = buffer.toString().split(":")[7];
		String pwd = buffer.toString().split(":")[8];
				
		//获取图片的名称（不包含服务端路径的图片名称）
        String[] strs1 = avatar1.split("/");
        String avatar = strs1[strs1.length - 1];
        String[] strs2 = bg1.split("/");
        String bg = strs2[strs2.length - 1];
        
		System.out.println("avatar:"+avatar);
		System.out.println("name:"+name);
		System.out.println("phone:"+phone);
		System.out.println("gender:"+gender);
		System.out.println("birthday:"+birthday);
		System.out.println("area:"+area);
		System.out.println("intro:"+intro);
		System.out.println("bg:"+bg);
		System.out.println("pwd:"+pwd);

		//向数据库修改数据
		DBUtil dbUtil = new DBUtil();
		//根据用户信息拼接sql语句
		String sql1 = "UPDATE users SET password = '"+pwd+"',avatar = '"+avatar+"',name = '"+name+"',gender = '"+gender+"',birthday = '"+
					   birthday+"',area = '"+area+"',profile = '"+intro+"',background = '"+bg+"' WHERE phone='"+phone+"'";
		System.out.println(sql1);
		int b = 0;
		try {
			b = dbUtil.updateDataToTable(sql1);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(b > 0) {
			//用户信息修改成功
			out.write("YES".getBytes());
		}else {
			//用户信息修改失败
			out.write("NO".getBytes());
		}
		out.flush();
		in.close();
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
