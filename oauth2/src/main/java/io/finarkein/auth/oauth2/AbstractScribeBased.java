package io.finarkein.auth.oauth2;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import io.finarkein.auth.oauth2.spi.ApiProvider;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Objects;

@Getter
@Accessors(fluent = true)
abstract class AbstractScribeBased extends FinarkeinCredentials {

    protected final transient OAuth20Service service;

    private final String clientId;
    private final String clientSecret;
    private final String authServerUri;
    private final String tokenServerUri;

    protected AbstractScribeBased(AccessToken accessToken, String clientId, String clientSecret, String authServerUri, String tokenServerUri) {
        super(accessToken);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authServerUri = authServerUri;
        this.tokenServerUri = tokenServerUri;
        this.service = new ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .build(ApiProvider.get().provide(authServerUri, tokenServerUri));
    }

    @Override
    @SneakyThrows
    public AccessToken refreshAccessToken() throws IOException {
        AccessToken accessToken = getAccessToken();
        if (Objects.isNull(accessToken)) {
            return fetchToken();
        }
        String refreshToken = accessToken.getRefreshToken();
        if (refreshToken == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() +
                    " instance cannot refresh because there is no refresh token.");
        }
        OAuth2AccessToken token = service.refreshAccessToken(refreshToken);
        return new AccessToken(token);
    }

    /**
     * Called when access token is not set yet!.
     *
     * @return fetched access token
     */
    protected abstract AccessToken fetchToken();

}
