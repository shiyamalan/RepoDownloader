package se.cambio.repository.common;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class DirectoryFinder
{

  public static File getJarDir(@SuppressWarnings("rawtypes") Class aclass)
  {
    URL url;
    String extURL; //  url.toExternalForm();

    // get an url
    try
    {
      url = aclass.getProtectionDomain().getCodeSource().getLocation();

    }
    catch (SecurityException ex)
    {
      url = aclass.getResource(aclass.getSimpleName() + ".class");
      // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
      //          file:/U:/Fred/java/Tools/UI/build/classes
      //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    // convert to external form
    extURL = url.toExternalForm();

    try
    {
      url = new URL(extURL);
    }
    catch (MalformedURLException mux)
    {
      // leave url unchanged; probably does not happen
    }

    // convert url to File
    try
    {
      return new File(url.toURI());
    }
    catch (URISyntaxException ex)
    {
      return new File(url.getPath());
    }
  }
}
