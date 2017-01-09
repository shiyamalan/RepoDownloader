package se.cambio.repository.downloader.service.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import se.cambio.repository.downloader.cmd.ContentReader;
import se.cambio.repository.downloader.service.entities.Repository;

public class FetchingDBManager implements Downloadable
{

  private static int READY = 1;

  private static final String SELECT_TABLE_SQL = "SELECT * FROM $TABLE_NAME WHERE " + column_names[1] + "<" + READY;

  private static Map<String, Repository> downloading_repositories;

  private static FetchingDBManager instance;
  
  public final static Logger logger = Logger.getLogger(FetchingDBManager.class);

  static
  {
    downloading_repositories = new HashMap<String, Repository>();
  }

  private FetchingDBManager()
  {

  }

  private static Repository getRepository(ResultSet rs, int ready) throws SQLException
  {
    Repository aRepo = new Repository();
    aRepo.repo_name = rs.getString(column_names[0]);
    aRepo.ready = ready;
    aRepo.repo_url = rs.getString(column_names[2]);
    aRepo.error = rs.getString(column_names[3]);
    return aRepo;
  }

  public Map<String, Repository> retriveRepos(String tableName) throws SQLException
  {
    Statement statement = MySQLConnector.getInstance().getConnection().createStatement();

    String sql = SELECT_TABLE_SQL.replace("$TABLE_NAME", tableName);
    logger.info("Fetching the " + TABLES[0] + " table for repositories urls.....");
    ResultSet rs = statement.executeQuery(sql);
    downloading_repositories.clear();
    while (rs.next())
    {
      int ready = rs.getInt(column_names[1]);
      if (rs.getString(column_names[2]) != null)
      {
        Repository aRepo = getRepository(rs, ready);
        aRepo.repo_url = ContentReader.getNormalizedName(ContentReader.getNormalizedURL(aRepo.repo_url));
        downloading_repositories.put(aRepo.repo_name, aRepo);
      }
    }
    rs.close();
    return downloading_repositories;
  }

  public static FetchingDBManager getInstance()
  {
    if (instance == null)
      instance = new FetchingDBManager();
    return instance;
  }

  public void update_a_field(String table_name, Repository repository) throws SQLException
  {
  }

  public void delete_a_row(String id)
  {

  }
}
