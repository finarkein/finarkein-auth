package io.finarkein.auth.oauth2;

import com.google.common.io.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class DefaultCredentialsProvider {

    private static final String CREDENTIAL_ENV_VAR = "FINARKEIN_API_CREDENTIALS";
    private static final String WELL_KNOWN_CREDENTIALS_FILE = "finarkein_credentials.json";

    // Below variable(s) should only be accessed inside a synchronized block
    private FinarkeinCredentials cachedCredentials = null;

    /**
     * Returns the Default Credentials.
     *
     * @return the credentials instance.
     */
    public FinarkeinCredentials getDefaultCredentials() throws IOException {
        synchronized (this) {
            if (cachedCredentials == null) {
                cachedCredentials = getDefaultCredentialsInternal();
            }
            if (cachedCredentials != null) {
                return cachedCredentials;
            }
        }

        throw new IOException(
                String.format(
                        "Could not locate Finarkein Credentials. You may set environment variable %s " +
                                "pointing to a file defining the credentials.",
                        CREDENTIAL_ENV_VAR));
    }

    private FinarkeinCredentials getDefaultCredentialsInternal() throws IOException {
        // First try the environment variable
        FinarkeinCredentials credentials = null;
        String credentialsPath = System.getenv(CREDENTIAL_ENV_VAR);
        if (credentialsPath != null && credentialsPath.length() > 0) {
            InputStream credentialsStream = null;
            try {
                File credentialsFile = new File(credentialsPath);
                if (!credentialsFile.isFile()) {
                    // Path will be put in the message from the catch block below
                    throw new IOException("File does not exist.");
                }
                credentialsStream = readStream(credentialsFile);
                credentials = FinarkeinCredentials.fromStream(credentialsStream);
            } catch (IOException e) {
                throw new IOException(
                        String.format(
                                "Error reading credential file from environment variable %s, value '%s': %s",
                                CREDENTIAL_ENV_VAR, credentialsPath, e.getMessage()),
                        e);
            } finally {
                if (credentialsStream != null) {
                    credentialsStream.close();
                }
            }
        }

        // try the well-known file
        if (credentials == null) {
            File knownFileLocation = getKnownCredentialsFile();
            InputStream credentialsStream = null;
            try {
                if (knownFileLocation.isFile()) {
                    // String.format("Attempting to load credentials from well known file: %s", knownFileLocation.getCanonicalPath())
                    credentialsStream = readStream(knownFileLocation);
                    credentials = FinarkeinCredentials.fromStream(credentialsStream);
                }
            } catch (IOException e) {
                throw new IOException(
                        String.format(
                                "Error reading credential file from location %s: %s",
                                knownFileLocation, e.getMessage()));
            } finally {
                if (credentialsStream != null) {
                    credentialsStream.close();
                }
            }
            // TODO: warn about usage of user credentials
        }

        return credentials;
    }

    private InputStream readStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    private File getKnownCredentialsFile() {
        URL url = Resources.getResource(WELL_KNOWN_CREDENTIALS_FILE);
        return new File(url.getPath());
    }

}
