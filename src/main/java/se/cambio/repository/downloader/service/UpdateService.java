package se.cambio.repository.downloader.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import se.cambio.repository.downloader.config.Configuration;
import se.cambio.repository.downloader.config.ConfigManager;
import se.cambio.repository.downloader.service.db.Table;
import se.cambio.repository.downloader.service.db.UpdateDBManager;
import se.cambio.repository.downloader.service.entities.Repository;

public class UpdateService implements Service
{
  public Map<String, Repository> updatingContents;

  public final static Logger logger = Logger.getLogger(UpdateService.class);

  public static boolean isUpdated = false;

  private static Service instance;

  private UpdateService()
  {

  }

  public static Service getInstance()
  {
    if (instance == null)
    {
      synchronized (DownloadService.class)
      {
        if (instance == null)
          instance = new UpdateService();
      }
    }
    return instance;
  }

  public boolean updateDB(Map<String, Repository> repos)
  {
    try
    {
      Properties props = ConfigManager.readPropertyFile(Configuration.REPOSITORY_PATH,
          Configuration.REPOSITORY_STATUS_FILE);
      for (Map.Entry<String, Repository> repo : repos.entrySet())
      {
        if (props.containsKey(repo.getValue().repo_name))
        {
          String repo_status = props.getProperty(repo.getValue().repo_name);
          repo.getValue().downloading_status = repo_status;
          repo.getValue().ready = Repository.getRepositoryStatus(repo_status);
          UpdateDBManager.getInstance().update_a_field(Table.TABLES[0], repo.getValue());
          logger.info("Repository: " + repo.getValue().repo_name + " has been updated in " + Table.TABLES[0]);
        }
      }
    }
    catch (IOException e)
    {
      return false;
    }
    catch (SQLException e)
    {
      logger.error("Update Failed in Table " + Table.TABLES[0] + "\n" + e.toString());
      return false;
    }
    logger.info("Repositories updates completed in " + Table.TABLES[0]);
    return true;
  }

  public void run()
  {
    isUpdated = updateDB(updatingContents);

  }

}
