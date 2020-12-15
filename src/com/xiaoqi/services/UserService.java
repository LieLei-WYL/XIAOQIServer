package com.xiaoqi.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoqi.entitys.Note;
import com.xiaoqi.entitys.User;
import com.xiaoqi.utils.DBUtil;

public class UserService {
	private List<User> users;
	private DBUtil dbUtil;

	public UserService() {
		try {
			//��ʼ���û�����
			users = new ArrayList<User>();
			dbUtil = DBUtil.getInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�����û���Ϣ
	 * @param sql ��ѯ�û���sql���
	 * @return �û�����
	 */
	public List<User> getUsers(String sql){
		try {
			ResultSet rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				String phone = rs.getString("phone");
				String password = rs.getString("password");
				String avatar = rs.getString("avatar");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				String birthday = rs.getString("birthday");
				String area = rs.getString("area");
				String profile = rs.getString("profile");
				String background = rs.getString("background");
				String search = rs.getString("search");
				User user = new User(phone,password,avatar,name,gender,birthday,area,profile,background,search);
				users.add(user);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	/**
	 * ����û�
	 * @param User ����ӵ��û�����
	 * @return ����û��Ƿ�ɹ����ɹ�����true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addUser(User user) throws ClassNotFoundException, SQLException {
		//��д�����û����ݵ�SQL���
		String sql1 = "insert into users(phone,password,name) values('"+user.getPhone()+"','"+user.getPassword()+"','"+user.getName()+"')";
		//����DBUtil��������ݷ���
		int n = dbUtil.updateDataToTable(sql1);
		//�ж������Ƿ����
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * �����û�phoneɾ��ָ���û�
	 * @param phone �û�phone
	 * @return �Ƿ�ɾ���û����ɹ�ɾ������true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteUserByPhone(String phone) throws ClassNotFoundException, SQLException {
		//��дɾ���ʼ����ݵ�SQL���
		String sql1 = "delete from users where phone = '" + phone + "'";
		//����DBUtil��ɾ�����ݷ���
		int n = dbUtil.updateDataToTable(sql1);
		//�ж������Ƿ�ɾ���ɹ�
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * �����û�phone�޸�ָ���û���Ϣ
	 * @param phone �û�phone
	 * @return �Ƿ��޸��û����ɹ��޸ķ���true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean updateUserByPhone(String sql) throws ClassNotFoundException, SQLException {
		//����DBUtil���޸����ݷ���
		 if(dbUtil.updateDataToTable(sql) != 0) {//��Ӱ��������Ϊ0��˵���޸ĳɹ�
			 return true;
		 } else {
			 return false;
		 }
	}
}
