package com.xiaoqi.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoqi.entitys.Comment;
import com.xiaoqi.entitys.Note;
import com.xiaoqi.utils.DBUtil;

public class CommentService {
	private List<Comment> comments;
	private DBUtil dbUtil;

	public CommentService() {
		try {
			//初始化笔记集合
			comments = new ArrayList<Comment>();
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
	 * 获取指定笔记的所有评论信息
	 * @param sql 查询指定笔记评论的sql语句
	 * @return 评论集合
	 */
	public List<Comment> getComments(String sql){
		try {
			ResultSet rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				int commentId = rs.getInt("comment_id");
				int noteId = rs.getInt("note_id");
				String phone = rs.getString("phone");
				String content = rs.getString("content");
				String date = rs.getString("date");
				String likes = rs.getString("likes");
				Comment comment = new Comment(commentId,noteId,phone,content,date,likes);
				comments.add(comment);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return comments;
	}
	
	/**
	 * 添加评论
	 * @param comment 待添加的评论对象
	 * @return 添加评论是否成功，成功返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addComment(Comment comment) throws ClassNotFoundException, SQLException {
		//编写插入笔记数据的SQL语句
		String sql1 = "insert into comments(note_id,phone,content,date,likes) values('"+comment.getNoteId()+"','"+comment.getPhone()+"','"+comment.getContent()+"','"+comment.getDate()+"',0)";
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
	 * 根据评论Id删除指定评论
	 * @param noteId 评论Id
	 * @return 是否删除笔记，成功删除返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteCommentById(String commentId) throws ClassNotFoundException, SQLException {
		//编写删除笔记数据的SQL语句
		String sql = "delete from comments where comment_id = '" + commentId + "'";
		//调用DBUtil的删除数据方法
		int n = dbUtil.updateDataToTable(sql);
		//判断数据是否删除成功
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
