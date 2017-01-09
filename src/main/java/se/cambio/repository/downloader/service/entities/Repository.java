package se.cambio.repository.downloader.service.entities;

import se.cambio.repository.downloader.service.db.Table;

public class Repository
{
  public String repo_name;
  public int ready;
  public String repo_url;
  public String error;
  public String downloading_status;
  
  @Override
  public String toString()
  {
    String result = "";
    
    result = Table.column_names[0] +" = '" + repo_name +"', " +Table.column_names[1] + " = '" + ready +"', " /*+ Table.column_names[2] + " = '" + repo_url
        +"', "*/ + Table.column_names[3] + " = '" + error + "', " + Table.column_names[4] + " = '" + downloading_status + "'";
    return result;
  }
  
  public static int getRepositoryStatus(String statusString)
  {
    if(statusString.equalsIgnoreCase("Successfully Downloaded"))
      return 1;
//    else if(statusString.equalsIgnoreCase("Already exists or Downloaded Previously"))
//      return 1;
    return 0;
  }
}
