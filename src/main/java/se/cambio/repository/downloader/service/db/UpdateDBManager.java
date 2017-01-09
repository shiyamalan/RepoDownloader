package se.cambio.repository.downloader.service.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import se.cambio.repository.downloader.service.entities.Repository;

public class UpdateDBManager implements Updateable
{
  private static String SQL = "UPDATE " + "$TABLE_NAME " + "SET " + " $COLUMNS " + " WHERE " + Table.column_names[0]
      + " = '" + "$VALUE" + "'";

  private static UpdateDBManager instance;

  private UpdateDBManager()
  {
  }

  public void update_a_field(String table_name, Repository repository) throws SQLException
  {
    Connection connection = MySQLConnector.getInstance().getConnection();
    Statement stmt = connection.createStatement();

    SQL = SQL.replace("$TABLE_NAME", table_name);
    SQL = SQL.replace("$COLUMNS", repository.toString());
    SQL = SQL.replace("$VALUE", repository.repo_name);

    stmt.executeUpdate(SQL);
  }

  public static UpdateDBManager getInstance()
  {
    if (instance == null)
      instance = new UpdateDBManager();
    return instance;
  }

  public void delete_a_row(String id)
  {
  }

}
