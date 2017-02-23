package data;

/**
 * Created by Sandeep.K on 24-02-2017.
 */
public class DriveAuthRequest {

    private String refreshToken;
    private String clientId;
    private String clientSecret;
    private String grantType;

    public DriveAuthRequest(String refreshToken, String clientId, String clientSecret) {
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = "refresh_token";
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRequestBody(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("refresh_token=" + refreshToken);
        stringBuilder.append("&client_id=" + clientId);
        stringBuilder.append("&client_secret=" + clientSecret);
        stringBuilder.append("&grant_type=" + grantType);
        return stringBuilder.toString();
    }
}
