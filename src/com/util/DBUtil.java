package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBUtil {
	private Connection conn;
	private String driver;
	private String url;
	private String username;
	private String password;

	public DBUtil() {
		Properties ps = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("src/default-in.txt");
			ps.load(fis);
			driver = ps.getProperty("driver");
			url = ps.getProperty("url");
			username = ps.getProperty("username");
			password = ps.getProperty("password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public boolean update(String sql, Object[] objs) {
		PreparedStatement pstm = null;
		try {
			pstm = this.conn.prepareStatement(sql);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++) {
					pstm.setObject(i + 1, objs[i]);
				}
			}
			int count = pstm.executeUpdate();
			if (count > 0) {
				conn.commit();
				return true;
			}
			conn.rollback();
			return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public <T> List<T> select(String sql, Object[] objs, Class<T> class1) {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = this.conn.prepareStatement(sql);
			if (objs != null) {
				for (int i = 0; i < objs.length; i++) {
					pstm.setObject(i + 1, objs[i]);
				}
			}
			List<T> ans = new ArrayList<T>();
			rs = pstm.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Method[] methods = class1.getMethods();
			List<Method> setMethod = new ArrayList<Method>();
			for (Method m : methods) {
				if (m.getName().startsWith("set")) {
					setMethod.add(m);
				}
			}
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				T t = class1.newInstance();
				for (int i = 1; i <= columnCount; i++) {
					for(Method sm : setMethod) {
						String set = sm.getName().toLowerCase().substring(3);
						if(set.equalsIgnoreCase(metaData.getColumnName(i))) {
							sm.invoke(t, rs.getObject(i).toString());
						}
					}
				}
				ans.add(t);
			}
			return ans;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
