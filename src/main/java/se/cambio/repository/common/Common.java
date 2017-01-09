package se.cambio.repository.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import se.cambio.repository.downloader.App;
import se.cambio.repository.downloader.cmd.CMDRunner;

public class Common
{
  public static final String DIRECTORY = "C:" + File.separator;

  public static final String SCRIPT_FOLDER_DIRECTORY = "scripts" + File.separator + "codebrag" + File.separator;

  public static final String LOG_FOLDER_DIRECTORY=DIRECTORY+"logs"+File.separator+"repository-downloader"+File.separator;

  public static final String SCRIPT_FILE_COPYING_LOCATION = DIRECTORY + SCRIPT_FOLDER_DIRECTORY;

  public static final String RESOURCE_DIRECTORY = DirectoryFinder.getJarDir(CMDRunner.class).getPath() + File.separator;

  public static final String SCRIPT_FILE_NAME = "git-svn-partial-clone-v5.sh";

  public static final String SCRIPT_FILE_PATH = RESOURCE_DIRECTORY + SCRIPT_FILE_NAME;

  public static String getOneYearBacks(Date currentDate)
  {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
    long milliseconds = (long) 365 * 24 * 60 * 60 * 1000;
    String stringDates = "";
    for (int i = 0; i < 10; i++)
    {
      Date oneYearBefore = new Date(currentDate.getTime() - milliseconds);
      stringDates += sdfDate.format(oneYearBefore) + ",";

      currentDate = oneYearBefore;
    }
    ;
    return stringDates;
  }

  public static void copyFileTo(String sourceLocation, String targetLocation, String fileName) throws IOException
  {
    System.out.println("Jar Location "  + sourceLocation);
    InputStream sourceStream = App.class.getResourceAsStream("/" + fileName);
    File dest = new File(targetLocation);
    try
    {
      FileUtils.copyInputStreamToFile(sourceStream, dest);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  private static File createAFolder(String filePath)
  {
    File file = new File(filePath);
    if(!file.exists())
      file.mkdirs();
    return file;
  }
  
  public static File createAFile(String fileName,String folder) throws IOException
  {
    File folderPath = createAFolder(folder);
    File file = new File(folderPath,fileName);
    if(!file.exists())
      file.createNewFile();
    return file;
    
  }
  public static Date getCurrentDate()
  {
    Date now = new Date();
    return now;
  }

}
