# Finarkein Auth [![Build Status](https://travis-ci.org/finarkein/finarkein-auth.svg?branch=main)](https://travis-ci.org/finarkein/finarkein-auth)

Finarkein authentication client for Java.

## Quickstart

Simply add the required dependency in your `pom.xml` to fetch from [Maven Central](https://search.maven.org/search?q=g:io.finarkein.auth):

```xml
<dependencies>
    <dependency>
      <groupId>io.finarkein.auth</groupId>
      <artifactId>finarkein-auth-oauth2</artifactId>
      <version>0.3.0</version>
    </dependency>
</dependencies>
```

### Use in your project

> `FinarkeinCredentials.java`, by default, looks for `finarkein_credentials.json` file in classpath. If it's found, it's automatically loaded.

#### Create a file named `finarkein_credentials.json` under `src/main/resources` project directory. For client credentials, the file will have following contents:
```json
{
  "type": "client_credentials",
  "client_id": "sample-client",
  "client_secret": "sample-client-a4814bdb-secret",
  "auth_uri": "https://demo.org/auth/",
  "token_uri": "https://demo.org/token"
}
``` 
#### Next, as shown in [FinarkeinCredentialsTest.java](oauth2/src/test/java/io/finarkein/auth/oauth2/FinarkeinCredentialsTest.java), use `FinarkeinCredentials` to obtain access token.

Following sample code shows usage with Spring WebClient:
```java
// Get credentials instance 
FinarkeinCredentials credentials = FinarkeinCredentials.get();
// Build new WebClient with filter to attach Authorization Headers
WebClient wc = WebClient.builder()
    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    .filter((request, next) -> {
        try {
            Map<String, List<String>> metadata = credentials.getRequestMetadata();
            /*
             * The metadata is structured as below:
             * "Authorization" -> [ "Bearer <TOKEN>" ]
             */
            ClientRequest.Builder builder = ClientRequest.from(request);
            for (Map.Entry<String, List<String>> entry : metadata.entrySet()) {
                String header = entry.getKey();
                List<String> values = entry.getValue();
                values.stream().findFirst().ifPresent(value -> builder.header(header, value));
            }
            return next.exchange(builder.build());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get access token", e);
        }
    })
    .baseUrl("https://api-base.com/")
    .build();
```

### Install from Github Packages

To install these artifacts from Github Packages, you'll need to configure `~/.m2/settings.xml` with your Github's [personal access token](https://github.com/settings/tokens):

```xml
<servers>
    <server>
        <id>github</id>
        <username>username</username>
        <password>personal_access_token</password>
    </server>
</servers>
```

And further, configure `<repositories>` either in your **.m2/settings.xml** or **pom.xml**.

```xml
<repositories>
    <repository>
        <id>github</id> <!-- ensure this matches your settings.xml / server ID -->
        <url>https://maven.pkg.github.com/finarkein/finarkein-auth</url>
    </repository>
</repositories>
```
