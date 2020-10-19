package io.finarkein.auth.oauth2;

import lombok.extern.log4j.Log4j2;

@Log4j2
class DefaultCredentialsProvider {

    private static final String CREDENTIAL_ENV_VAR = "FINARKEIN_API_CREDENTIALS";
    private static final String WELL_KNOWN_CREDENTIALS_FILE = "finarkein_credentials.json";

    // These variables should only be accessed inside a synchronized block
    private FinarkeinCredentials cachedCredentials = null;

    /**
     * Returns the Default Credentials.
     *
     * @return
     */
    public FinarkeinCredentials getDefaultCredentials() {
        return null;
    }
}
