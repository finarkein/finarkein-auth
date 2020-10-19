package io.finarkein.auth.oauth2.spi;

import com.github.scribejava.core.builder.api.DefaultApi20;

import java.util.ServiceLoader;

public interface ApiProvider {

    static ApiProvider get() {
        ApiProvider resolvedProvider = null;
        for (ApiProvider provider : ServiceLoader.load(ApiProvider.class)) {
            resolvedProvider = provider;
        }

        if (resolvedProvider == null)
            resolvedProvider = new DefaultApiProvider();

        return resolvedProvider;
    }

    DefaultApi20 provide(String authUri, String tokenUri);

}
