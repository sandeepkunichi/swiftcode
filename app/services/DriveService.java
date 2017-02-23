package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import data.Document;
import data.DriveAuthRequest;
import data.DriveAuthResponse;
import play.Configuration;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sandeep.K on 4/8/2016.
 */
public class DriveService {

    /** Application name. */
    private static final String APPLICATION_NAME = "SwiftCode";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart.json
     */
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize(DriveAuthResponse driveAuthResponse) throws Exception {

        InputStream in = DriveService.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        Credential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(HTTP_TRANSPORT)
                .setClientSecrets(clientSecrets).build();

        credential.setAccessToken(driveAuthResponse.getAccessToken());
        credential.setRefreshToken(driveAuthResponse.getRefreshToken());

        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService(Configuration configuration, WSClient wsClient) throws Exception {
        Credential credential = authorize(getDriveAuthResponse(configuration, wsClient));
        return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public static File insertFile(Document document) throws IOException {
        File body = new File();
        body.setName(document.getTitle());
        body.setDescription(document.getDescription());
        body.setMimeType(document.getMimeType());
        body.setParents(Collections.singletonList(document.getParentId()));
        FileContent mediaContent = new FileContent(document.getMimeType(), document.getFile());
        return document.getService().files().create(body, mediaContent).execute();
    }

    public static DriveAuthResponse getDriveAuthResponse(Configuration configuration, WSClient wsClient) throws ExecutionException, InterruptedException {

        DriveAuthRequest driveAuthRequest = new DriveAuthRequest(
                configuration.getString("drive.refresh_token"),
                configuration.getString("drive.client_id"),
                configuration.getString("drive.client_secret")
        );

        CompletableFuture<JsonNode> wsRequest = wsClient
                .url(configuration.getString("drive.auth_endpoint"))
                .setContentType("application/x-www-form-urlencoded")
                .post(driveAuthRequest.getRequestBody())
                .thenApply(WSResponse::asJson)
                .toCompletableFuture();
        try {
            wsRequest.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DriveAuthResponse(wsRequest.get().get("access_token").asText(), driveAuthRequest.getRefreshToken());
    }

}
