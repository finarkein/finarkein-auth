package io.finarkein.auth.oauth2.spi;


import com.github.scribejava.core.builder.api.DefaultApi20;

class DefaultApiProvider implements ApiProvider {

    @Override
    public DefaultApi20 provide(String authUri, String tokenUri) {
        return new InternalApi(authUri, tokenUri);
    }

    private static class InternalApi extends DefaultApi20 {

        private final String authUri;
        private final String tokenUri;

        InternalApi(String authUri, String tokenUri) {
            this.authUri = authUri;
            this.tokenUri = tokenUri;
        }

        @Override
        public String getAccessTokenEndpoint() {
            return tokenUri;
        }

        @Override
        protected String getAuthorizationBaseUrl() {
            return authUri;
        }
    }

}
