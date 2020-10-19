package io.finarkein.auth.oauth2;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FinarkeinCredentialsTest {

    @Test
    void testGet() throws IOException {
        FinarkeinCredentials credentials = FinarkeinCredentials.get();
        Assertions.assertNotNull(credentials);
    }

}
