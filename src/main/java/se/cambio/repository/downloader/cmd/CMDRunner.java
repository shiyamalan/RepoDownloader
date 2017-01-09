package se.cambio.repository.downloader.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import se.cambio.repository.common.Common;
import se.cambio.repository.downloader.App;
import se.cambio.repository.downloader.config.ConfigManager;

public class CMDRunner
{
  public static final String SERVICE_CMD = "net";

  public static String SERVICE_NAME = "";

  public static String STOP_SERVICE_CMD = SERVICE_CMD + " stop " + "$SERVICE_NAME";

  public static String START_SERVICE_CMD = SERVICE_CMD + " start " + "$SERVICE_NAME";

  public static final String SCRIPT_COMMAND = "sh";

  public static final String COMMAND_SEPARATOR = " ";

  public static String REPO_PATH;

  public static String REPO_URL;

  public static String BRANCHES;

  public static String SCRIPT_FILE_PATH;

  public static String DOWNLOAD_DATE;

  public static void runSHCMD(String args) throws IOException, InterruptedException
  //public static void main(String args[]) throws IOException, InterruptedException
  {
    String command_args[] = args.split(" ");

    REPO_PATH = command_args[0]; // folder name should be created inside the running dir

    REPO_URL = command_args[1]; //repository url

    BRANCHES = command_args[2]; //branch names with comma separated

    SCRIPT_FILE_PATH = Common.SCRIPT_FILE_COPYING_LOCATION.concat(Common.SCRIPT_FILE_NAME);

    DOWNLOAD_DATE = command_args[3];

    System.out.println("------Script File Path : " + SCRIPT_FILE_PATH + "------");

    run(true, "");
  }

  private static void run(boolean isShCmd, String cmdArgs) throws IOException, InterruptedException
  {
    
    //File file = Common.createAFile("repository-status.txt", Common.LOG_FOLDER_DIRECTORY);
    //start a cmd / shell-instance (also works with /bin/sh)
    final Process process = Runtime.getRuntime().exec("cmd");
    //handle the data of the input stream (non-blocking)
    pipe(process.getInputStream(), System.out);
    //handle the data of the error stream (non-blocking)
    pipe(process.getErrorStream(), System.err);

    //pipe(process.getInputStream(), new FileOutputStream(file,true));
    //pass the commands to the output stream
    PrintWriter commands = new PrintWriter(process.getOutputStream(), true);

    if (isShCmd)
    {
      commands.println(SCRIPT_COMMAND + COMMAND_SEPARATOR + SCRIPT_FILE_PATH + COMMAND_SEPARATOR + REPO_PATH
          + COMMAND_SEPARATOR + REPO_URL + COMMAND_SEPARATOR + BRANCHES + COMMAND_SEPARATOR + DOWNLOAD_DATE);

    }
    else
    {
      //run the service command on command line
      commands.println(cmdArgs);
    }
    ///commands.println("sh " + "");
    commands.close();

    //wait for the process to close
    process.waitFor();
  }

  public static void runRestartServiceCMD() throws IOException, InterruptedException
  {
    //restart the existing the 
    if(App.serviceName.equals("") || App.serviceName.equals(null))
      SERVICE_NAME = ConfigManager.getPorpsValue(1);
    STOP_SERVICE_CMD  = STOP_SERVICE_CMD.replace("$SERVICE_NAME", SERVICE_NAME);
    START_SERVICE_CMD = START_SERVICE_CMD.replace("$SERVICE_NAME", SERVICE_NAME);
    
    run(false, STOP_SERVICE_CMD); //stop the running service
    run(false, START_SERVICE_CMD);//start the service back
  }

  private static void pipe(final InputStream from, final OutputStream to)
  {
    new Thread(new Runnable()
    {

      public void run()
      {
        int length;
        byte[] buffer = new byte[128];
        try
        {
          while ((length = from.read(buffer)) != -1)
            to.write(buffer, 0, length);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        finally
        {
          try
          {
            from.close();
          }
          catch (IOException e)
          {
            /* ... */ }
        }

      }

    }).start();
  }

}
