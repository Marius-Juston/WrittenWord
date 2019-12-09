package writtenword.google;

import com.google.common.collect.ImmutableList;
import com.google.photos.library.v1.PhotosLibraryClient;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author Marius Juston
 **/
public class Photos {

  public static final String credentialsPath = "credentials.json";
  private static final List<String> REQUIRED_SCOPES =
      ImmutableList.of(
          "https://www.googleapis.com/auth/photoslibrary.readonly",
          "https://www.googleapis.com/auth/photoslibrary.appendonly");
  private final static Photos instance = new Photos();

  private Photos() {
    try {
      PhotosLibraryClient client = PhotosLibraryClientFactory.createClient(credentialsPath, REQUIRED_SCOPES);
    } catch (IOException | GeneralSecurityException e) {
      e.printStackTrace();
    }
  }

  public static Photos getInstance() {
    return instance;
  }
}
