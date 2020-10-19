package io.finarkein.auth.oauth2;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

public class UserCredentials extends AbstractScribeBased {

    protected UserCredentials(AccessToken accessToken, OAuth20Service service) {
        super(accessToken, service);
    }

    @SneakyThrows
    public static UserCredentials fromJson(JsonObject json) {
        String clientId = json.get("client_id").getAsString();
        String clientSecret = json.get("client_secret").getAsString();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
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
        OAuth2AccessToken token = service.getAccessTokenPasswordGrant(username, password);
        return new UserCredentials(new AccessToken(token), service);
    }

}
