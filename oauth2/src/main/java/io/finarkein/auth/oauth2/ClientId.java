package io.finarkein.auth.oauth2;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.util.Map;

/**
 * An OAuth2 user authorization Client ID and associated information.
 *
 * <p>Corresponds to the information in the json file downloadable for a Client ID.
 */
@Getter
@Accessors(fluent = true)
public class ClientId {

    private static final String FIELD_CLIENT_ID = "client_id";
    private static final String FIELD_CLIENT_SECRET = "client_secret";
    private static final String JSON_PARSE_ERROR = "Error parsing Client ID JSON: ";

    private final String clientId;
    private final String clientSecret;

    /**
     * Constructs a client ID using an explicit ID and secret
     *
     * <p>Note: Direct use of this constructor in application code is not recommended to avoid having
     * secrets or values that need to be updated in source code.
     *
     * @param clientId     Text identifier of the Client ID.
     * @param clientSecret Secret to associated with the Client ID.
     */
    private ClientId(String clientId, String clientSecret) {
        this.clientId = Preconditions.checkNotNull(clientId);
        this.clientSecret = clientSecret;
    }

    /**
     * Constructs a client ID from an explicit ID and secret.
     *
     * <p>Note: Direct use of this factory method in application code is not recommended to avoid
     * having secrets or values that need to be updated in source code.
     *
     * @param clientId     Text identifier of the Client ID.
     * @param clientSecret Secret to associated with the Client ID.
     * @return The ClientId instance.
     */
    public static ClientId of(String clientId, String clientSecret) {
        return new ClientId(clientId, clientSecret);
    }

    /**
     * Constructs a Client ID from JSON from a downloaded file.
     *
     * @param json The JSON from the downloaded file.
     * @return the ClientId instance based on the JSON.
     * @throws IOException The JSON could not be parsed.
     */
    public static ClientId fromJson(Map<String, Object> json) throws IOException {
        String clientId = OAuth2Utils.validateString(json, FIELD_CLIENT_ID, JSON_PARSE_ERROR);
        if (clientId.length() == 0) {
            throw new IOException(
                    "Unable to parse ClientId. Field '" + FIELD_CLIENT_ID + "' is required.");
        }
        String clientSecret =
                OAuth2Utils.validateOptionalString(json, FIELD_CLIENT_SECRET, JSON_PARSE_ERROR);
        return new ClientId(clientId, clientSecret);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder {

        private String clientId;

        private String clientSecret;

        protected Builder() {
            // noop
        }

        protected Builder(ClientId clientId) {
            this.clientId = clientId.clientId();
            this.clientSecret = clientId.clientSecret();
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public ClientId build() {
            return new ClientId(clientId, clientSecret);
        }
    }
}
