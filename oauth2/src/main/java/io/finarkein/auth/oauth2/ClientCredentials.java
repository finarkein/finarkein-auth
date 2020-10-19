package io.finarkein.auth.oauth2;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import java.io.IOException;

public class ClientCredentials extends AbstractScribeBased {

    protected ClientCredentials(AccessToken accessToken, String clientId, String clientSecret, String authUri, String tokenUri) {
        super(accessToken, clientId, clientSecret, authUri, tokenUri);
    }

    public static ClientCredentials fromJson(JsonObject json) throws IOException {
        String clientId = json.get("client_id").getAsString();
        String clientSecret = json.get("client_secret").getAsString();
        String authUri = json.get("auth_uri").getAsString();
        String tokenUri = json.get("token_uri").getAsString();
        if (clientId == null || clientSecret == null) {
            throw new IOException(
                    "Error reading user credential from JSON, "
                            + " expecting 'client_id' and 'client_secret'.");
        }
        return new ClientCredentials(null, clientId, clientSecret, authUri, tokenUri);
    }

    @Override
    @SneakyThrows
    protected AccessToken fetchToken() {
        OAuth2AccessToken token = service.getAccessTokenClientCredentialsGrant();
        return new AccessToken(token);
    }
}
