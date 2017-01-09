package se.cambio.repository.downloader.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.cambio.repository.downloader.App;

/**
 * 
 * @author SRatnavel
 *Reading the text file given in the argument
 */
public class ContentReader
{

  public static int total_text_file_line = 0;

  public final static String REPLACING_STRING = "branches";

  public static List<String> readFile(File file)
  {
    List<String> contents = new ArrayList<String>();
    if (file == null)
    {
      System.out.println("************ The Given File " + App.content_path + " doesn't exists************");
      return contents;
    }
    System.out.println("************Java program reading the file contents from " + file.getPath() + "************");
    BufferedReader br = null;

    try
    {

      String currentLineText;
      total_text_file_line = 0;
      Reader reader = new FileReader(file);
      br = new BufferedReader(reader);

      while ((currentLineText = br.readLine()) != null)
      {
        if (currentLineText.contains("https://"))
        {
          contents.add(currentLineText);
          total_text_file_line += 1;
        }
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (br != null)
          br.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }

    return contents;
  }

  public static Map<String, String> getUrlsWithBranch(File file)
  {
    System.out.println("************Java Mapping the file contents with repository URLs************");
    List<String> contents = readFile(file);
    Map<String, String> urlMap = new HashMap<String, String>();

    for (String url : contents)
    {
      String split_contents[] = url.split(" ");
      if (split_contents != null)
      {
        String temp = getURL(split_contents);
        temp = getNormalizedURL(temp);
        //get the branch Name
        String branchName = temp.substring(temp.lastIndexOf("/") + 1);
        temp = getNormalizedName(temp);

        if (urlMap.containsKey(temp))
        {
          urlMap.put(temp, urlMap.get(temp).concat(",").concat(branchName));
        }
        else
        {
          urlMap.put(temp, branchName);
        }
      }
    }

    return urlMap;

  }

  private static String getURL(String[] split_contents)
  {
    for(String content:split_contents)
    {
      if(content.contains("https://") || content.contains("http://"))
        return content;
    }
    return null;
  }

  public static String getNormalizedName(String name)
  {
    //name.substring(start, name.lastIndexOf("/")).replace(replaceFor, "");
    if(name == null || !name.contains("/"))
      return name;
    name = name.replace(REPLACING_STRING,"");
    int end = name.lastIndexOf("//");
    return  name.substring(0, end);
  }
  
  public static String getNormalizedURL(String temp)
  {
    if(temp == null || temp.equals(""))
      return null;
    if (temp.lastIndexOf("/") == temp.length() - 1)
      temp = temp.substring(0, temp.length() - 1);//remove the last '/' character if contain
    
    return temp;
  }
}
