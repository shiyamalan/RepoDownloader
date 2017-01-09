package se.cambio.repository.downloader.service.db;

public interface Table
{
  static final String TABLES[] = { "repository_statuses" };

  static String column_names[] = { "repository_name", "ready", "repo_url", "error", "downloading_status" };
  
}
