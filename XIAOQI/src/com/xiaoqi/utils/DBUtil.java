package com.xiaoqi.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtil {
	// 连接SqlServer的配置信息
	private static String driver;
	private static String connStr;
	private static String user;
	private static String pwd;

	private static Connection conn = null;
	
	//定义当前类的对象作为属性（单例模式需要）
	private static DBUtil dbUtil;
	
	//提供获取当前类的对象的接口
	public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
		if(null == dbUtil) {
			dbUtil = new DBUtil();
		}
		return dbUtil;
	}

	//为了自动执行connectToDB() getConnection()方法的解决方案：静态代码块
	static {
		// 加载配置文件，初始化SqlServer配置属性
		try {
			loadDBProperty("DBConfig.properties");
			connectToDB();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载数据库配置文件
	 * 
	 * @param pFile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
		// 创建Properties对象
		Properties prop = new Properties();
		// 加载配置文件
		prop.load(DBUtil.class.getClassLoader().getResourceAsStream(pFile));
		driver = prop.getProperty("DRIVER");
		connStr = prop.getProperty("CONN_STR");
		user = prop.getProperty("USER");
		pwd = prop.getProperty("PWD");
	}

	// 获取连接对象
	private static void connectToDB() throws SQLException, ClassNotFoundException {
		if (null == conn || conn.isClosed()) {
			Class.forName(driver);
			conn = DriverManager.getConnection(connStr, user, pwd);
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param sql 查询数据的sql语句
	 * @return 查询到的数据集
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
//		// 连接到数据库
//		connectToDB();
		Statement stmt = conn.createStatement();
		// 执行查询
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	/**
	 * 判断数据是否存在
	 * 
	 * @param sql 查询的sql语句
	 * @return 存在则返回true，否则返回false
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
//		// 连接到数据库
//		connectToDB();
		Statement stmt = conn.createStatement();
		// 执行查询
		ResultSet rs = stmt.executeQuery(sql);
		return rs.next();
	}

	/**
	 * 插入、修改或删除数据
	 * 
	 * @param sql 需要执行sql语句
	 * @return 插入、修改、删除记录的行数
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int updateDataToTable(String sql) throws ClassNotFoundException, SQLException {
//		// 连接到数据库
//		connectToDB();
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void closeDB() throws SQLException {
		if (null != conn && !conn.isClosed()) {
			conn.close();
		}
	}
}
