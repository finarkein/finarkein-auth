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

    public static ClientCredentials.Builder newBuilder() {
        return new ClientCredentials.Builder();
    }

    @Override
    @SneakyThrows
    protected AccessToken fetchToken() {
        OAuth2AccessToken token = service.getAccessTokenClientCredentialsGrant();
        return new AccessToken(token);
    }

    public static class Builder extends FinarkeinCredentials.Builder {

        private String clientId;
        private String clientSecret;
        private String authServerUri;
        private String tokenServerUri;

        protected Builder() {
            // noop.
        }

        protected Builder(ClientCredentials credentials) {
            this.clientId = credentials.clientId();
            this.clientSecret = credentials.clientSecret();
            this.setAccessToken(credentials.getAccessToken());
            this.authServerUri = credentials.authServerUri();
            this.tokenServerUri = credentials.tokenServerUri();
        }

        @Override
        public ClientCredentials.Builder setAccessToken(AccessToken token) {
            super.setAccessToken(token);
            return this;
        }

        public ClientCredentials.Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientCredentials.Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public ClientCredentials.Builder setAuthServerUri(String authServerUri) {
            this.authServerUri = authServerUri;
            return this;
        }

        public ClientCredentials.Builder setTokenServerUri(String tokenServerUri) {
            this.tokenServerUri = tokenServerUri;
            return this;
        }

        @Override
        public ClientCredentials build() {
            return new ClientCredentials(
                    getAccessToken(),
                    clientId,
                    clientSecret,
                    authServerUri,
                    tokenServerUri);
        }

    }
}
