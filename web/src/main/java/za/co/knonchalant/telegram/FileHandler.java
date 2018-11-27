package za.co.knonchalant.telegram;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * I feel strongly like we're going to start serving images, so I kept this file here
 * even though it's unused. See also DownloadServlet.
 */
public class FileHandler {

    public static byte[] fetchRemoteFile(String location)  {
        URL url = null;
        try {
            url = new URL(location.replaceAll(" ", "%20"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        byte[] bytes = null;
        try(InputStream is = url.openStream ()) {
            ;
            bytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
            //handle errors
            e.printStackTrace();
        }
        return bytes;
    }

}
