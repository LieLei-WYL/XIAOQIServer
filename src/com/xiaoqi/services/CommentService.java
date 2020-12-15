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
			//��ʼ���ʼǼ���
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
	 * ��ȡָ���ʼǵ�����������Ϣ
	 * @param sql ��ѯָ���ʼ����۵�sql���
	 * @return ���ۼ���
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
	 * �������
	 * @param comment ����ӵ����۶���
	 * @return ��������Ƿ�ɹ����ɹ�����true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addComment(Comment comment) throws ClassNotFoundException, SQLException {
		//��д����ʼ����ݵ�SQL���
		String sql1 = "insert into comments(note_id,phone,content,date,likes) values('"+comment.getNoteId()+"','"+comment.getPhone()+"','"+comment.getContent()+"','"+comment.getDate()+"',0)";
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
	 * ��������Idɾ��ָ������
	 * @param noteId ����Id
	 * @return �Ƿ�ɾ���ʼǣ��ɹ�ɾ������true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteCommentById(String commentId) throws ClassNotFoundException, SQLException {
		//��дɾ���ʼ����ݵ�SQL���
		String sql = "delete from comments where comment_id = '" + commentId + "'";
		//����DBUtil��ɾ�����ݷ���
		int n = dbUtil.updateDataToTable(sql);
		//�ж������Ƿ�ɾ���ɹ�
		if(n != 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
