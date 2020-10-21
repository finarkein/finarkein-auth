# Finarkein Auth [![Build Status](https://travis-ci.org/finarkein/finarkein-auth.svg?branch=main)](https://travis-ci.org/finarkein/finarkein-auth)

Finarkein authentication client for Java.

## Quickstart

Simply add the required dependency in your `pom.xml` to fetch from [Maven Central](https://search.maven.org/search?q=g:io.finarkein.auth):

```xml
<dependencies>
    <dependency>
      <groupId>io.finarkein.auth</groupId>
      <artifactId>finarkein-auth-credentials</artifactId>
      <version>0.3.0</version>
    </dependency>
    
    <dependency>
      <groupId>io.finarkein.auth</groupId>
      <artifactId>finarkein-auth-oauth2</artifactId>
      <version>0.3.0</version>
    </dependency>
</dependencies>
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
