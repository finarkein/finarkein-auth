package io.finarkein.auth.oauth2;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

public class ClientCredentials extends AbstractScribeBased {

    protected ClientCredentials(AccessToken accessToken, OAuth20Service service) {
        super(accessToken, service);
    }

    @SneakyThrows
    public static ClientCredentials fromJson(JsonObject json) {
        String clientId = json.get("client_id").getAsString();
        String clientSecret = json.get("client_secret").getAsString();
        String authUri = json.get("auth_uri").getAsString();
        String tokenUri = json.get("token_uri").getAsString();
        OAuth20Service service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .build(new DefaultApi20() {
                    @Override
                    public String getAccessTokenEndpoint() {
                        return tokenUri;
                    }

                    @Override
                    protected String getAuthorizationBaseUrl() {
                        return authUri;
                    }
                });
        OAuth2AccessToken token = service.getAccessTokenClientCredentialsGrant();
        return new ClientCredentials(new AccessToken(token), service);
    }
}
