package se.cambio.repository.downloader.service;

import java.io.IOException;

import org.apache.log4j.Logger;

import se.cambio.repository.common.Common;

public class ServiceInitiator
{
  public final static Logger logger = Logger.getLogger(ServiceInitiator.class);

  public static void main(String args[])
  {
    Service service = DownloadService.getInstance();
    try
    {
      Common.copyFileTo(Common.SCRIPT_FILE_PATH, Common.SCRIPT_FILE_COPYING_LOCATION.concat(Common.SCRIPT_FILE_NAME),
          Common.SCRIPT_FILE_NAME);
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }
    Thread thread = new Thread(service);

    if (!thread.isAlive())
    {
      logger.info("\r\n************************* Repository Downloader Started ***********************");
      thread.start();

    }
    else
    {
      logger.info("Downloading part not completed yet previous task is being executed! so scheduled to next time");
      return;
    }
  }
}
