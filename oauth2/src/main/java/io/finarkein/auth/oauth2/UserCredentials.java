package io.finarkein.auth.oauth2;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * TODO: Use refresh_token instead of `username` & `password`!
 */
public class UserCredentials extends AbstractScribeBased {

    private final String username;
    private final String password;

    protected UserCredentials(String clientId, String clientSecret, String username, String password, AccessToken accessToken,
                              String authServerUri, String tokenServerUri) {
        super(accessToken, clientId, clientSecret, authServerUri, tokenServerUri);
        this.username = username;
        this.password = password;
    }

    @SneakyThrows
    public static UserCredentials fromJson(JsonObject json) {
        String clientId = json.get("client_id").getAsString();
        String clientSecret = json.get("client_secret").getAsString();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        String authUri = json.get("auth_uri").getAsString();
        String tokenUri = json.get("token_uri").getAsString();

        if (clientId == null || clientSecret == null || username == null || password == null) {
            throw new IOException(
                    "Error reading user credential from JSON, "
                            + " expecting 'client_id', 'client_secret', 'username' and 'password'.");
        }

        return UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setUsername(username)
                .setPassword(password)
                .setAuthServerUri(authUri)
                .setTokenServerUri(tokenUri)
                .build();
    }

    public static UserCredentials.Builder newBuilder() {
        return new UserCredentials.Builder();
    }

    @Override
    @SneakyThrows
    protected AccessToken fetchToken() {
        OAuth2AccessToken token = service.getAccessTokenPasswordGrant(username, password);
        return new AccessToken(token);
    }

    public static class Builder extends FinarkeinCredentials.Builder {

        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
        private String authServerUri;
        private String tokenServerUri;

        protected Builder() {
            // noop.
        }

        protected Builder(UserCredentials credentials) {
            this.clientId = credentials.clientId();
            this.clientSecret = credentials.clientSecret();
            this.username = credentials.username;
            this.password = credentials.password;
            this.setAccessToken(credentials.getAccessToken());
            this.authServerUri = credentials.authServerUri();
            this.tokenServerUri = credentials.tokenServerUri();
        }

        @Override
        public Builder setAccessToken(AccessToken token) {
            super.setAccessToken(token);
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setAuthServerUri(String authServerUri) {
            this.authServerUri = authServerUri;
            return this;
        }

        public Builder setTokenServerUri(String tokenServerUri) {
            this.tokenServerUri = tokenServerUri;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public UserCredentials build() {
            return new UserCredentials(
                    clientId,
                    clientSecret,
                    username,
                    password,
                    getAccessToken(),
                    authServerUri,
                    tokenServerUri);
        }


    }

}
