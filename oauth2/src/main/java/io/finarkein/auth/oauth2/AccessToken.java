package io.finarkein.auth.oauth2;

import com.github.scribejava.core.model.OAuth2AccessToken;
import io.finarkein.auth.util.Clock;

import java.util.Objects;

public class AccessToken {

    private final OAuth2AccessToken token;
    private final Long expirationTimeMillis;

    public AccessToken(OAuth2AccessToken token) {
        this.token = token;
        this.expirationTimeMillis = expirationTimeInMillis();
    }

    private Long expirationTimeInMillis() {
        if (Objects.nonNull(token.getExpiresIn())) {
            long expiresInMillis = token.getExpiresIn() * 1000L; // token.getExpiresIn (seconds)
            return Clock.SYSTEM.currentTimeMillis() + expiresInMillis;
        }
        return null;
    }

    public String getAccessToken() {
        return token.getAccessToken();
    }

    public String getTokenType() {
        return token.getTokenType();
    }

    public Integer getExpiresIn() {
        return token.getExpiresIn();
    }

    public String getRefreshToken() {
        return token.getRefreshToken();
    }

    public String getScope() {
        return token.getScope();
    }

    public Long getExpirationTime() {
        return expirationTimeMillis;
    }
}
