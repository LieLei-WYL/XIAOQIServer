package com.xiaoqi.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaoqi.utils.DBUtil;

/**
 * ��ͻ��˷�����ҳ�Ƽ��ʼ�
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
		try {
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
			
			String search = strs[0];
			String phone = strs[1];
			
			if(search != null) {
				String[] strings = search.split("��");
				int size = strings.length;
				String newSearch = strings[size-1];
				System.out.println("��������:"+newSearch);
				if(!newSearch.equals("")) {
					//��ѯ���������Ƿ��Ѵ���
					sql = "select * from hotsearch WHERE search='"+newSearch+"'";
					System.out.println(sql);
					if(dbUtil.isExist(sql)) {//�Ѵ���
						sql = "select * from hotsearch WHERE search='"+newSearch+"'";
						System.out.println(sql);
						rs = dbUtil.queryDate(sql);
						rs.next();
						int num = rs.getInt("num") + 1;
						sql = "UPDATE hotsearch SET num = '"+num+"' WHERE search='"+newSearch+"'";
						System.out.println(sql);
						if(dbUtil.updateDataToTable(sql) != 0) {//��Ӱ��������Ϊ0��˵���޸ĳɹ�
							System.out.println("���������޸ĳɹ�");
						} else {
							System.out.println("���������޸�ʧ��");
						}
					} else {
						sql = "insert into hotsearch(search,num) values('"+newSearch+"',1)";
						System.out.println(sql);
						if(dbUtil.updateDataToTable(sql) != 0) {//��Ӱ��������Ϊ0��˵���޸ĳɹ�
							System.out.println("�����ʴ����ɹ�");
						} else {
							System.out.println("�����ʴ���ʧ��");
						}
					}
				}
			}
			
			sql = "UPDATE users SET search = '"+search+"' WHERE phone='"+phone+"'";
			System.out.println(sql);
			if(dbUtil.updateDataToTable(sql) != 0) {//��Ӱ��������Ϊ0��˵���޸ĳɹ�
				System.out.println("�޸ĳɹ�");
			} else {
				System.out.println("�޸�ʧ��");
			}
			//���ظ��ͻ���
//			out.write("OK".getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
