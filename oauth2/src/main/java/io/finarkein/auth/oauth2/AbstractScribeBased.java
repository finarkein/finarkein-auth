package io.finarkein.auth.oauth2;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.SneakyThrows;

import java.io.IOException;

abstract class AbstractScribeBased extends FinarkeinCredentials {

    private final transient OAuth20Service service;

    protected AbstractScribeBased(AccessToken accessToken, OAuth20Service service) {
        super(accessToken);
        this.service = service;
    }

    @Override
    @SneakyThrows
    public AccessToken refreshAccessToken() throws IOException {
        String refreshToken = getAccessToken().getRefreshToken();
        if (refreshToken == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() +
                    " instance cannot refresh because there is no refresh token.");
        }
        OAuth2AccessToken token = service.refreshAccessToken(refreshToken);
        return new AccessToken(token);
    }

}
