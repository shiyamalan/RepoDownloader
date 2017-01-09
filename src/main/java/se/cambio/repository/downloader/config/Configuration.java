package se.cambio.repository.downloader.config;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Configuration
{

  private static Logger logger = Logger.getLogger(ConfigManager.class);

  public static final String CONFIG_PROPERTY_FILE_KEYS[] = { "repoPath", "serviceName", "dbName", "dbUser", "dbPassword" };

  public static final String CONFIGURATION_PROPERTY_FILE_DIR = System.getProperty("user.dir");

  public static final String REPOSITORY_STATUS_FILE = "status.properties";

  public static final String CONFIGURATION_FILE_NAME = "config.properties";

  public static String REPOSITORY_PATH = "";

  public static String getRepositoryPath(String args[])
  {
    return args[0];
  }

  public static String getServiceName(String args[])
  {
    return args[1];
  }

  public static void updateFile(File file, String fileName)
  {
    try
    {
      ConfigManager.updateResourceFile(new File(file.getPath(), fileName));
    }
    catch (IOException e)
    {
      logger.error("File Creation or Deletion Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
