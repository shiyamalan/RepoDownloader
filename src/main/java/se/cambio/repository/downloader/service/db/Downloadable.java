package se.cambio.repository.downloader.service.db;

import java.sql.SQLException;
import java.util.Map;

import se.cambio.repository.downloader.service.entities.Repository;

public interface Downloadable extends Table
{
  Map<String,Repository> retriveRepos(String tableName) throws SQLException;
}
