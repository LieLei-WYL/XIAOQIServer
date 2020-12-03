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
			//��ʼ���ʼǼ���
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
	 * ��ȡ���бʼ���Ϣ
	 * @param sql ��ѯ�ʼǵ�sql���
	 * @return �ʼǼ���
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
	 * ��ӱʼ�
	 * @param Note ����ӵıʼǶ���
	 * @return ��ӱʼ��Ƿ�ɹ����ɹ�����true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean addNote(Note note) throws ClassNotFoundException, SQLException {
		//��д����ʼ����ݵ�SQL���
		String sql1 = "insert into notes(phone,images,title,content,topic,date,area) values('"+note.getAuthorPhone()+"','"+note.getImages()+"','"+note.getTitle()+"','"+note.getContent()+"','"+note.getTopic()+"','"+note.getDate()+"','"+note.getArea()+"')";
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
	 * ���ݱʼ�Idɾ��ָ���ʼ�
	 * @param noteId �ʼ�Id
	 * @return �Ƿ�ɾ���ʼǣ��ɹ�ɾ������true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean deleteNoteById(String noteId) throws ClassNotFoundException, SQLException {
		//��дɾ���ʼ����ݵ�SQL���
		String sql1 = "delete from notes where note_id = '" + noteId + "'";
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
	 * ���ݱʼ�Id�޸�ָ���ʼ�
	 * @param noteId �ʼ�Id
	 * @return �Ƿ��޸ıʼǣ��ɹ��޸ķ���true�����򷵻�false
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public boolean updateNoteByName(String sql) throws ClassNotFoundException, SQLException {
		//����DBUtil���޸����ݷ���
		 if(dbUtil.updateDataToTable(sql) != 0) {//��Ӱ��������Ϊ0��˵���޸ĳɹ�
			 return true;
		 } else {
			 return false;
		 }
	}
}
