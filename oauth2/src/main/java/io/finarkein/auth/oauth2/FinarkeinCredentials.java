package io.finarkein.auth.oauth2;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Base type for credentials to authorize calls to Finarkein APIs using OAuth2.
 */
public class FinarkeinCredentials extends OAuth2Credentials {

    private static final transient DefaultCredentialsProvider defaultCredentialsProvider =
            new DefaultCredentialsProvider();

    /**
     * Default constructor.
     */
    protected FinarkeinCredentials() {
        this(null);
    }

    public FinarkeinCredentials(AccessToken accessToken) {
        super(accessToken);
    }

    public static FinarkeinCredentials get() throws IOException {
        return defaultCredentialsProvider.getDefaultCredentials();
    }

    /**
     * Returns credentials defined by a JSON file stream.
     *
     * @param credentialsStream the stream with the credential definition.
     * @return the credential defined by the credentialsStream.
     * @throws IOException if the credential cannot be created from the stream.
     */
    public static FinarkeinCredentials fromStream(InputStream credentialsStream) throws IOException {
        Preconditions.checkNotNull(credentialsStream);

        try (InputStreamReader reader = new InputStreamReader(credentialsStream)) {
            JsonObject fileContents = JsonParser.parseReader(reader).getAsJsonObject();

            String fileType = fileContents.get("type").getAsString();
            if (fileType == null) {
                throw new IOException("Error reading credentials from stream, 'type' field not specified.");
            }
            if ("password".equals(fileType)) {
                return UserCredentials.fromJson(fileContents);
            }
            if ("client_credentials".equals(fileType)) {
                return ClientCredentials.fromJson(fileContents);
            }
            throw new IOException(
                    String.format(
                            "Error reading credentials from stream, 'type' value '%s' not recognized."
                                    + " Expecting '%s' or '%s'.",
                            fileType, "password", "client_credentials"));
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder extends OAuth2Credentials.Builder {
        protected Builder() {
            // noop.
        }

        protected Builder(FinarkeinCredentials credentials) {
            setAccessToken(credentials.getAccessToken());
        }

        public FinarkeinCredentials build() {
            return new FinarkeinCredentials(getAccessToken());
        }

        @Override
        public Builder setAccessToken(AccessToken token) {
            super.setAccessToken(token);
            return this;
        }
    }

}
