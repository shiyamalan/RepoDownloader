package se.cambio.repository.downloader.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager
{
  static Properties properties;

  static
  {
    try
    {
      properties = readPropertyFile(Configuration.CONFIGURATION_PROPERTY_FILE_DIR,
          Configuration.CONFIGURATION_FILE_NAME);

      Configuration.REPOSITORY_PATH = getPorpsValue(0);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  public static Properties readPropertyFile(String filePath, String file_name) throws IOException
  {
    Properties properties = new Properties();
    InputStream input;

    File file = new File(getFilePath(filePath, file_name));
    if (!file.exists())
    {
      throw new FileNotFoundException("The Property File " + file_name + " is not in " + filePath);
    }
    input = new FileInputStream(file);
    properties.load(input);
    return properties;
  }

  public static String getFilePath(String filePath, String file_name)
  {
    return filePath + File.separator + getFileName(file_name);
  }

  public static String getPorpsValue(int i)
  {
    return properties.get(Configuration.CONFIG_PROPERTY_FILE_KEYS[i]).toString();
  }

  public static String getFileName(String file_name)
  {
    return file_name.equals("") || file_name.equals(null) ? Configuration.CONFIGURATION_FILE_NAME : file_name;
  }

  public static void updateResourceFile(File file) throws IOException
  {
    if ((file.exists()))
    {
      file.delete();
    }
    file.createNewFile();
  }
}
