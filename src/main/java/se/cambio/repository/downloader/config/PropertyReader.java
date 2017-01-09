package se.cambio.repository.downloader.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
	public static final String PROPERTY_FILE_KEYS[] = { "repoPath", "serviceName"};

	public static String property_file_location = "";

	public static String file_name = "";
	
	

	public static Properties properties;

	static
	{
		try {
		  property_file_location = System.getProperty("user.dir");
		  setFileName("config.properties");
			properties = readPropertyFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static Properties readPropertyFile() throws IOException {
		Properties properties = new Properties();
		InputStream input;

		File file = new File(getFilePath());
		if (!file.exists())
		{
			throw new FileNotFoundException(
					"The Property File " + file_name + " is not in " + property_file_location);
		}
		input = new FileInputStream(file);
		properties.load(input);
		return properties;
	}

	public static String getFilePath() {
		return property_file_location + File.separator + getFileName();
	}
	
	public static void setFilePath(String rootFilePath)
	{
	  property_file_location = rootFilePath;
	}
	public static String getPorpsValue(int i) {
		return properties.get(PROPERTY_FILE_KEYS[i]).toString();
	}
	
	public static void setFileName(String name)
	{
	  if(name.equals("") || name.equals(null))
	  {
	    file_name = "config.properties";return;
	  } 
	  file_name = name;
	}
	public static String getFileName()
	{
	  return file_name.equals("") || file_name.equals(null) ? "config.properties" : file_name;
	}
	
	public static void updateResourceFile(File file) throws IOException
	{
	  if((file.exists()))
	  {
	    file.delete();
	  }
	  file.createNewFile();
	}
}
