package se.cambio.repository.downloader.service.db;

import java.sql.SQLException;

import se.cambio.repository.downloader.service.entities.Repository;

public interface Updateable extends Table
{
  void update_a_field(String table_name,Repository repository) throws SQLException;
  
  void delete_a_row(String id);
}
