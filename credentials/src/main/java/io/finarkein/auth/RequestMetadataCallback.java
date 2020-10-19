package io.finarkein.auth;

import java.util.List;
import java.util.Map;

/**
 * The callback that receives the result of the asynchronous {@link
 * Credentials#getRequestMetadata(java.net.URI, java.util.concurrent.Executor,
 * RequestMetadataCallback)}. Exactly one method should be called.
 */
public interface RequestMetadataCallback {
    /**
     * Called when metadata is successfully produced.
     *
     * @param metadata Metadata returned for the request.
     */
    void onSuccess(Map<String, List<String>> metadata);

    /**
     * Called when metadata generation failed.
     *
     * @param exception The thrown exception which caused the request metadata fetch to fail.
     */
    void onFailure(Throwable exception);
}
