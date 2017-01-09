package se.cambio.repository.downloader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import se.cambio.repository.common.Common;
import se.cambio.repository.downloader.cmd.ContentReader;

public class App
{

  public static String content_path = "";

  public static String serviceName = "";

  public static void main(String[] args)
  {
    String repo_url = "";
    String branches = "";
    String repo_root_path = null; //where the repositories are going to store eg E:\\Repositories\\repo\\
    int count = 1;
    if ((args.length != 3))
    {
      System.out.println(
          "The argument should contain the \\files\\filename.txt , \\repositories\\repo folder path and ServiceName");
      System.out.println("--------Help-------");
      System.out.println("Argument1:" + "repositories_url.txt");
      System.out.println("Argument2:" + "root path of repositories");
      System.out.println("Argument3:" + "service name has to restart after download all repos");
    }

    App.serviceName = args[2];
    try
    {
      Common.copyFileTo(Common.SCRIPT_FILE_PATH, Common.SCRIPT_FILE_COPYING_LOCATION.concat(Common.SCRIPT_FILE_NAME),
          Common.SCRIPT_FILE_NAME);
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }
    App.content_path = args[0];
    repo_root_path = args[1];

    File file = new File(args[0]);
    Map<String, String> urlsMap = ContentReader.getUrlsWithBranch(file);

    for (Entry<String, String> entry : urlsMap.entrySet())
    {
      String key = entry.getKey().toString();
      String value = entry.getValue();
      String values[] = new String[0];

      repo_url = key; // downloading the url 
      branches = value; //separated branches with commas

      System.out.println("--------url, " + key + " branch name " + value + "----------");

      if (branches.contains(","))
        values = branches.split(",");
      else
      {
        values = new String[1];
        values[0] = branches;
      }
      for (String branch : values)
      {
        System.out.println("------------------------- Fetching is now at (current/total_repourl) " + "(" + count + "/"
            + ContentReader.total_text_file_line + ") ");
        String commandArgs = repo_root_path + " " + repo_url + " " + branch + " "
            + Common.getOneYearBacks(Common.getCurrentDate());
        RepoDownloader.startDownloadService(commandArgs);
        count++;
      }
    }

    RepoDownloader.restartService();

  }

}
