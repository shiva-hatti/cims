package com.iris.util.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iris.util.PropertiesBundle;
import com.iris.util.constant.ErrorConstants;

/**
 * This class control the various database operation
 * 
 * @author Suman Kumar
 * @version 1.0
 */
public class ConnectionPooling {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPooling.class);

	/** The connection object */
	private Connection conn;
	/** The prepared statement object */
	private PreparedStatement pstmt;
	/** The statement object */
	private Statement stmt;
	/** The result set object */
	private ResultSet rs;
	/** The callable statement */
	private CallableStatement clblStmt;

	private final String USER_NAME_KEY = "spring.datasource.username";
	private final String USER_PSWD_KEY = "spring.datasource.password";
	private final String DB_DRIVER_KEY = "jdbc.driverClassName";
	private final String DB_URL_KEY = "spring.datasource.url";
	private final String DB_RESOURCE_FILE = "application.properties";

	/**
	 * The constructor that would create the connection object after being called
	 */
	public ConnectionPooling() {
		getConnection();
	}

	/**
	 * This would create the new DB Connection
	 * 
	 * @return Connection The connection object
	 */
	public Connection getConnection() {
		try {
			if (conn != null && !conn.isClosed()) {
				return conn;
			}
		} catch (SQLException se) {
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), se);
		}
		try {
			PropertiesBundle propertiesBundle = null;
			try {
				propertiesBundle = PropertiesBundle.load(DB_RESOURCE_FILE);
			} catch (Exception e) {
				LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
			}
			if (propertiesBundle == null) {
				return null;
			}
			String userName = propertiesBundle.getProperty(USER_NAME_KEY);
			String userPswd = propertiesBundle.getProperty(USER_PSWD_KEY);
			String driverKey = propertiesBundle.getProperty(DB_DRIVER_KEY);
			String dbUrl = propertiesBundle.getProperty(DB_URL_KEY);

			Class.forName(driverKey);
			conn = DriverManager.getConnection(dbUrl, userName, userPswd);
		} catch (SQLException se) {
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), se);
		} catch (Exception e) {
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return conn;
	}

	/**
	 * This method would create the Prepared statement for the given query
	 * 
	 * @param query The query for which prepared statement is to be created
	 * @return PreparedStatement The prepared statement object
	 */
	public PreparedStatement getPreparedStatement(String query) {
		try {
			if (conn != null) {
				pstmt = conn.prepareStatement(query);
				conn.commit();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return pstmt;
	}

	/**
	 * This method would create the Statement object
	 * 
	 * @return Statement The statement object
	 */
	public Statement getStatement() {
		try {
			if (conn != null) {
				stmt = conn.createStatement();
			} else {
				stmt = null;
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return stmt;
	}

	/**
	 * This method would create the Scrollable statement
	 * 
	 * @return Statement The scrollable statement object
	 */
	public Statement getStmtRSScrollable() {
		try {
			if (conn != null) {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} else {
				stmt = null;
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return stmt;
	}

	/**
	 * The scrollable result set object for the given query
	 * 
	 * @param query The query for which scrollable result set is to be created
	 * @return ResultSet The scrollable result set object
	 */
	public ResultSet getRSScrollable(String query) {
		try {
			stmt = getStmtRSScrollable();
			if (stmt != null) {
				rs = stmt.executeQuery(query);
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return rs;
	}

	/**
	 * The result set object for the given query
	 * 
	 * @param query The query for which result set is to be created
	 * @return ResultSet The result set object
	 */
	public ResultSet getResultSet(String query) {
		try {
			stmt = getStatement();
			if (stmt != null) {
				rs = stmt.executeQuery(query);
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return rs;
	}

	/**
	 * This method would create the callable statement for the given query
	 * 
	 * @param param The query for which callable statement is to be created
	 * @return CallableStatement The callable statement objecct
	 */
	public CallableStatement getClblStmt(String param) {
		try {
			if (conn != null) {
				clblStmt = conn.prepareCall(param);
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return clblStmt;
	}

	/**
	 * This method would create the prepared statement result set for the given query
	 * 
	 * @param query The query for which prepared statement result set is to be created
	 * @return ResultSet The prepared statement result set object
	 */
	public ResultSet getPreResultSet(String query) {
		try {
			pstmt = getPreparedStatement(query);
			if (stmt != null) {
				rs = pstmt.executeQuery();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return rs;
	}

	/**
	 * This method would close the connection object created
	 */
	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		} finally {
			conn = null;
		}
	}

	/**
	 * This method would commit all the changes made to db for the current connection
	 */
	public void commit() {
		try {
			if (conn != null) {
				conn.commit();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}

	}

	/**
	 * This method would close the result set object
	 * 
	 * @param rs The result set object to be closed
	 * @return ResultSet The closed result set object
	 */
	public ResultSet closeRS(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return rs;
	}

	/**
	 * This method would close the statement object
	 * 
	 * @param myStmt The statement object to be closed
	 * @return Statement The closed statement object
	 */
	public Statement closeStmt(Statement myStmt) {
		try {
			if (myStmt != null) {
				myStmt.close();
			}
		} catch (Exception e) {
			// GlobalLogger.logApplicationErrorLog(ExceptionUtils.getFullStackTrace(e),
			// LOGGER);
			LOGGER.error(ErrorConstants.ERROR_MSG_CONTROLLER.getConstantVal(), e);
		}
		return myStmt;
	}

	//	public static void main(String[] args) {
	//		ConnectionPooling cp = new ConnectionPooling();
	//		System.out.println(cp.conn);
	//	}

}
