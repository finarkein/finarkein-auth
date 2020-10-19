package io.finarkein.auth.oauth2;

import com.google.common.base.Preconditions;
import com.google.common.net.HttpHeaders;
import io.finarkein.auth.Credentials;
import io.finarkein.auth.util.Clock;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base credentials type for OAuth2.
 */
public class OAuth2Credentials extends Credentials {

    private static final long MINIMUM_TOKEN_MILLISECONDS = 60000L /* 1L*/;
    private static final transient Clock clock = Clock.SYSTEM;

    private final Object lock = new byte[0];
    private Map<String, List<String>> requestMetadata;
    private AccessToken temporaryAccess;

    /**
     * Default constructor.
     */
    protected OAuth2Credentials() {
        this(null);
    }

    /**
     * Constructor with explicit access token.
     *
     * @param accessToken initial or temporary access token
     */
    protected OAuth2Credentials(AccessToken accessToken) {
        if (accessToken != null) {
            useAccessToken(accessToken);
        }
    }

    /**
     * Returns the credentials instance from the given access token.
     *
     * @param accessToken the access token
     * @return the credentials instance
     */
    public static OAuth2Credentials create(AccessToken accessToken) {
        return OAuth2Credentials.newBuilder().setAccessToken(accessToken).build();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean hasRequestMetadata() {
        return true;
    }

    public boolean hasRequestMetadataOnly() {
        return true;
    }

    /**
     * Get the current request metadata in a blocking manner, refreshing tokens if required.
     * <p>
     * Provide the request metadata by ensuring there is a current access token and providing it as an
     * authorization bearer token.
     *
     * <p>This should be called by the transport layer on each request, and the data should be
     * populated in headers or other context. The operation can block and fail to complete and may do
     * things such as refreshing access tokens.
     *
     * @param uri URI of the entry point for the request.
     * @return The request metadata used for populating headers or other context.
     * @throws IOException if there was an error getting up-to-date access.
     */
    public Map<String, List<String>> getRequestMetadata(URI uri) throws IOException {
        synchronized (lock) {
            if (shouldRefresh()) {
                refresh();
            }
            return Preconditions.checkNotNull(requestMetadata, "requestMetadata");
        }
    }

    // Must be called under lock
    private void useAccessToken(AccessToken token) {
        this.temporaryAccess = token;
        this.requestMetadata =
                Collections.singletonMap(
                        HttpHeaders.AUTHORIZATION,
                        Collections.singletonList(OAuth2Utils.BEARER_PREFIX + token.getAccessToken()));
    }

    /**
     * Refresh the token by discarding the cached token and metadata and requesting the new ones.
     */
    public void refresh() throws IOException {
        synchronized (lock) {
            temporaryAccess = null;
            useAccessToken(Preconditions.checkNotNull(refreshAccessToken(), "new access token"));
        }
    }

    /**
     * Refresh these credentials only if they have expired or are expiring imminently.
     *
     * @throws IOException during token refresh.
     */
    public void refreshIfExpired() throws IOException {
        synchronized (lock) {
            if (shouldRefresh()) {
                refresh();
            }
        }
    }

    // Must be called under lock
    // requestMetadata will never be null if false is returned.
    private boolean shouldRefresh() {
        Long expiresIn = getExpiresInMilliseconds();
        return requestMetadata == null || expiresIn != null && expiresIn <= MINIMUM_TOKEN_MILLISECONDS;
    }

    private Long getExpiresInMilliseconds() {
        if (temporaryAccess == null) {
            return null;
        }
        Long expirationTimeInMillis = temporaryAccess.getExpirationTime();
        if (expirationTimeInMillis == null) {
            return null;
        }
        return (expirationTimeInMillis - clock.currentTimeMillis());
    }

    /**
     * Method to refresh the access token according to the specific type of credentials.
     *
     * <p>Throws IllegalStateException if not overridden since direct use of OAuth2Credentials is only
     * for temporary or non-refreshing access tokens.
     *
     * @return Refreshed access token.
     * @throws IOException from derived implementations
     */
    public AccessToken refreshAccessToken() throws IOException {
        throw new IllegalStateException(
                "OAuth2Credentials instance does not support refreshing the"
                        + " access token. An instance with a new access token should be used, or a derived type"
                        + " that supports refreshing.");
    }

    /**
     * Returns the cached access token.
     *
     * <p>If not set, you should call {@link #refresh()} to fetch and cache an access token.
     *
     * @return The cached access token.
     */
    public final AccessToken getAccessToken() {
        return temporaryAccess;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private AccessToken accessToken;

        protected Builder() {
        }

        protected Builder(OAuth2Credentials credentials) {
            this.accessToken = credentials.getAccessToken();
        }

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public Builder setAccessToken(AccessToken token) {
            this.accessToken = token;
            return this;
        }

        public OAuth2Credentials build() {
            return new OAuth2Credentials(accessToken);
        }
    }

}
