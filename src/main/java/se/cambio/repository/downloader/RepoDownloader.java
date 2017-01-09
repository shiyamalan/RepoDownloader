package se.cambio.repository.downloader;

import java.io.IOException;

import se.cambio.repository.downloader.cmd.CMDRunner;

public class RepoDownloader
{
    public static void startDownloadService(String commandArgs)
    {
      try
      {
        CMDRunner.runSHCMD(commandArgs);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    
    public static void restartService()
    {
      try
      {
        CMDRunner.runRestartServiceCMD();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
}
