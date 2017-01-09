package se.cambio.repository.downloader.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import se.cambio.repository.common.Common;
import se.cambio.repository.downloader.cmd.CMDRunner;
import se.cambio.repository.downloader.config.Configuration;
import se.cambio.repository.downloader.service.db.FetchingDBManager;
import se.cambio.repository.downloader.service.db.Table;
import se.cambio.repository.downloader.service.entities.Repository;

public class DownloadService implements Service
{
  public final static Logger logger = Logger.getLogger(DownloadService.class);

  private static Service instance;

  public void run()
  {
    try
    {

      String cmdArgs = "";

      Map<String, Repository> repos = downloadContents();

      logger.info("Starting to download the repositories.....");

      Configuration.updateFile(new File(Configuration.REPOSITORY_PATH), Configuration.REPOSITORY_STATUS_FILE);
      for (Map.Entry<String, Repository> rep : repos.entrySet())
      {
        logger.info("Downloading URl: " + rep.getValue().repo_url);

        logger.info("Downloading Repository: " + rep.getValue().repo_name);

        cmdArgs = Configuration.REPOSITORY_PATH + " " + rep.getValue().repo_url + " " + rep.getKey() + " "
            + Common.getOneYearBacks(Common.getCurrentDate());

        CMDRunner.runSHCMD(cmdArgs);

        logger.info("Downloaded completed For " + rep.getValue().repo_name);
      }
      if (repos.size() > 0)
        logger.info("Downloaded Finished");
      else
        logger.info("No Repositories Downloaded. All Repositories downloaded previously");
      logger.info("Uploading Task starting.....");

      CMDRunner.runRestartServiceCMD();
      updateDB(repos);
    }
    catch (SQLException e)
    {
      logger.error("" + e.getErrorCode() + " " + e.getMessage() + "\r\n" + e.toString());
    }
    catch (IOException e)
    {
      logger.error(e.getMessage() + "\r\n" + e.toString());
    }
    catch (InterruptedException e)
    {
      logger.error(e.getMessage() + "\r\n" + e.toString());
    }
  }

  private void updateDB(Map<String, Repository> repos)
  {
    Service service = UpdateService.getInstance();
    ((UpdateService) service).updatingContents = repos;
    Thread thread = new Thread(service);
    thread.start();

  }

  public Map<String, Repository> downloadContents() throws SQLException
  {
    return FetchingDBManager.getInstance().retriveRepos(Table.TABLES[0]);
  }

  public static Service getInstance()
  {
    if (instance == null)
    {
      synchronized (DownloadService.class)
      {
        if (instance == null)
          instance = new DownloadService();
      }
    }
    return instance;
  }

}
