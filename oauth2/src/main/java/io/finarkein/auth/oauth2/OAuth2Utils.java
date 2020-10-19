package io.finarkein.auth.oauth2;

import io.finarkein.auth.http.AuthHttpContants;

import java.io.IOException;
import java.util.Map;

public class OAuth2Utils {

    static final String BEARER_PREFIX = AuthHttpContants.BEARER + " ";

    private static final String VALUE_NOT_FOUND_MESSAGE = "%sExpected value %s not found.";
    private static final String VALUE_WRONG_TYPE_MESSAGE = "%sExpected %s value %s of wrong type.";

    static String validateString(Map<String, Object> map, String key, String errorPrefix)
            throws IOException {
        Object value = map.get(key);
        if (value == null) {
            throw new IOException(String.format(VALUE_NOT_FOUND_MESSAGE, errorPrefix, key));
        }
        if (!(value instanceof String)) {
            throw new IOException(String.format(VALUE_WRONG_TYPE_MESSAGE, errorPrefix, "string", key));
        }
        return (String) value;
    }

    /**
     * Return the specified optional string from JSON or throw a helpful error message.
     */
    static String validateOptionalString(Map<String, Object> map, String key, String errorPrefix)
            throws IOException {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new IOException(String.format(VALUE_WRONG_TYPE_MESSAGE, errorPrefix, "string", key));
        }
        return (String) value;
    }
}
