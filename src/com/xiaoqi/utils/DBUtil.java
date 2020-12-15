package com.xiaoqi.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	// ����SqlServer��������Ϣ
	private static String driver;
	private static String connStr;
	private static String user;
	private static String pwd;

	private static Connection conn = null;
	
	//���嵱ǰ��Ķ�����Ϊ���ԣ�����ģʽ��Ҫ��
	private static DBUtil dbUtil;
	
	//�ṩ��ȡ��ǰ��Ķ���Ľӿ�
	public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
		if(null == dbUtil) {
			dbUtil = new DBUtil();
		}
		return dbUtil;
	}

	//Ϊ���Զ�ִ��connectToDB() getConnection()�����Ľ����������̬�����
	static {
		// ���������ļ�����ʼ��SqlServer��������
		try {
			loadDBProperty("DBConfig.properties");
			connectToDB();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �������ݿ������ļ�
	 * 
	 * @param pFile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
		// ����Properties����
		Properties prop = new Properties();
		// ���������ļ�
		prop.load(DBUtil.class.getClassLoader().getResourceAsStream(pFile));
		driver = prop.getProperty("DRIVER");
		connStr = prop.getProperty("CONN_STR");
		user = prop.getProperty("USER");
		pwd = prop.getProperty("PWD");
	}

	// ��ȡ���Ӷ���
	private static void connectToDB() throws SQLException, ClassNotFoundException {
		if (null == conn || conn.isClosed()) {
			Class.forName(driver);
			conn = DriverManager.getConnection(connStr, user, pwd);
		}
	}

	/**
	 * ��ѯ����
	 * 
	 * @param sql ��ѯ���ݵ�sql���
	 * @return ��ѯ�������ݼ�
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
//		// ���ӵ����ݿ�
//		connectToDB();
		Statement stmt = conn.createStatement();
		// ִ�в�ѯ
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * �ж������Ƿ����
	 * 
	 * @param sql ��ѯ��sql���
	 * @return �����򷵻�true�����򷵻�false
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
//		// ���ӵ����ݿ�
//		connectToDB();
		Statement stmt = conn.createStatement();
		// ִ�в�ѯ
		ResultSet rs = stmt.executeQuery(sql);
		return rs.next();
	}

	/**
	 * ���롢�޸Ļ�ɾ������
	 * 
	 * @param sql ��Ҫִ��sql���
	 * @return ���롢�޸ġ�ɾ����¼������
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int updateDataToTable(String sql) throws ClassNotFoundException, SQLException {
//		// ���ӵ����ݿ�
//		connectToDB();
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}

	/**
	 * �ر����ݿ�����
	 * 
	 * @throws SQLException
	 */
	public void closeDB() throws SQLException {
		if (null != conn && !conn.isClosed()) {
			conn.close();
		}
	}
}
