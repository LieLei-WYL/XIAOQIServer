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
			//初始化用户集合
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
	 * 获取所有用户信息
	 * @param sql 查询用户的sql语句
	 * @return 用户集合
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
	 * 添加用户
	 * @param User 待添加的用户对象
	 * @return 添加用户是否成功，成功返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addUser(User user) throws ClassNotFoundException, SQLException {
		//编写插入用户数据的SQL语句
		String sql1 = "insert into users(phone,password,name) values('"+user.getPhone()+"','"+user.getPassword()+"','"+user.getName()+"')";
		//调用DBUtil的添加数据方法
		int n = dbUtil.updateDataToTable(sql1);
		//判断数据是否存在
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 根据用户phone删除指定用户
	 * @param phone 用户phone
	 * @return 是否删除用户，成功删除返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteUserByPhone(String phone) throws ClassNotFoundException, SQLException {
		//编写删除笔记数据的SQL语句
		String sql1 = "delete from users where phone = '" + phone + "'";
		//调用DBUtil的删除数据方法
		int n = dbUtil.updateDataToTable(sql1);
		//判断数据是否删除成功
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 根据用户phone修改指定用户信息
	 * @param phone 用户phone
	 * @return 是否修改用户，成功修改返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean updateUserByPhone(String sql) throws ClassNotFoundException, SQLException {
		//调用DBUtil的修改数据方法
		 if(dbUtil.updateDataToTable(sql) != 0) {//受影响行数不为0，说明修改成功
			 return true;
		 } else {
			 return false;
		 }
	}
}
