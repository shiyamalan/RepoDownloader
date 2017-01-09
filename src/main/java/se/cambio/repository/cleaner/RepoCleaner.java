package se.cambio.repository.cleaner;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import se.cambio.repository.downloader.config.ConfigManager;
import se.cambio.repository.downloader.config.Configuration;
import se.cambio.repository.downloader.service.entities.Repository;

public class RepoCleaner
{

  public static void cleanRepos()
  {
    try
    {
      Properties repos = ConfigManager.readPropertyFile(Configuration.REPOSITORY_PATH,
          Configuration.REPOSITORY_STATUS_FILE);
      for (Entry<Object, Object> repo : repos.entrySet())
      {
        String status = repo.getValue().toString();
        String repo_name = repo.getKey().toString();
        
        if(Repository.getRepositoryStatus(status) != 1)
        {
          
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
   
  }
  
  private void isMoreFileExists(String branchNameFolder)
  {
    File repoDir = new File(Configuration.REPOSITORY_PATH,branchNameFolder);
    
    File []files = repoDir.listFiles();
    
   // for()
    
    
  }
}
