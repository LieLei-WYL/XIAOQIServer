package com.xiaoqi.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xiaoqi.entitys.Note;
import com.xiaoqi.utils.DBUtil;

public class NoteService {
	private List<Note> notes;
	private DBUtil dbUtil;

	public NoteService() {
		try {
			//初始化笔记集合
			notes = new ArrayList<Note>();
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
	 * 获取所有笔记信息
	 * @param sql 查询笔记的sql语句
	 * @return 笔记集合
	 */
	public List<Note> getNotes(String sql){
		try {
			ResultSet rs = dbUtil.queryDate(sql);
			while(rs.next()) {
				int noteId = rs.getInt("note_id");
				String phone = rs.getString("phone");
				String images = rs.getString("images");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String topic = rs.getString("topic");
				String date = rs.getString("date");
				String area = rs.getString("area");
				Note note = new Note(noteId,phone,images,title,content,topic,date,area);
				notes.add(note);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return notes;
	}
	
	/**
	 * 添加笔记
	 * @param Note 待添加的笔记对象
	 * @return 添加笔记是否成功，成功返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addNote(Note note) throws ClassNotFoundException, SQLException {
		//编写插入笔记数据的SQL语句
		String sql1 = "insert into notes(phone,images,title,content,topic,date,area) values('"+note.getAuthorPhone()+"','"+note.getImages()+"','"+note.getTitle()+"','"+note.getContent()+"','"+note.getTopic()+"','"+note.getDate()+"','"+note.getArea()+"')";
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
	 * 根据笔记Id删除指定笔记
	 * @param noteId 笔记Id
	 * @return 是否删除笔记，成功删除返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteNoteById(String noteId) throws ClassNotFoundException, SQLException {
		//编写删除笔记数据的SQL语句
		String sql1 = "delete from notes where note_id = '" + noteId + "'";
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
	 * 根据笔记Id修改指定笔记
	 * @param noteId 笔记Id
	 * @return 是否修改笔记，成功修改返回true，否则返回false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean updateNoteByName(String sql) throws ClassNotFoundException, SQLException {
		//调用DBUtil的修改数据方法
		 if(dbUtil.updateDataToTable(sql) != 0) {//受影响行数不为0，说明修改成功
			 return true;
		 } else {
			 return false;
		 }
	}
}
