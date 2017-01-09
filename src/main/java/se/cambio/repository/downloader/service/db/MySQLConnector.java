package se.cambio.repository.downloader.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import se.cambio.repository.downloader.config.ConfigManager;

public class MySQLConnector
{
  private static MySQLConnector instance;

  private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  private static final String DB_NAME = ConfigManager.getPorpsValue(2);

  private static final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;

  //  Database credentials
  static final String USER_NAME =  ConfigManager.getPorpsValue(3);

  static final String PASSWORD = ConfigManager.getPorpsValue(4);

  static Connection conn = null;

  static Logger logger = Logger.getLogger(MySQLConnector.class);

  private MySQLConnector()
  {

  }

  public static MySQLConnector getInstance()
  {
    if (instance == null)
    {
      synchronized (MySQLConnector.class)
      {
        if (instance == null)
          instance = new MySQLConnector();
      }
    }
    return instance;
  }

  public Connection getConnection()
  {
    try
    {
      Class.forName(JDBC_DRIVER);
      if (conn == null)
      {
        logger.info("Connecting to " + DB_NAME + " .....");
        conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        logger.info("Connected to " +  DB_NAME + " database successfully"); 
      }
      return conn;
    }
    catch (SQLException se)
    {
      logger.error("SQL Connection Error: " + se.getMessage() + " " + se.toString());
      se.printStackTrace();
    }
    catch (Exception e)
    {
      logger.error("SQL Exception: " + e.getMessage() + " " + e.toString());
      e.printStackTrace();
    }
    return conn;
  }
}
