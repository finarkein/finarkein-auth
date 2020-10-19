# Finarkein Auth

Finarkein authentication client for Java.

## Quickstart

Simply add the required dependency to your pom.xml:

```xml
<dependencies>
    <dependency>
      <groupId>io.finarkein.auth</groupId>
      <artifactId>finarkein-auth-credentials</artifactId>
      <version>0.2.0</version>
    </dependency>
    
    <dependency>
      <groupId>io.finarkein.auth</groupId>
      <artifactId>finarkein-auth-oauth2</artifactId>
      <version>0.2.0</version>
    </dependency>
</dependencies>
```

If you have not configured access to Github Packages in your work environment. Please check [prerequisites](#prerequisites) section to learn more about it.

### Prerequisites

Since artifacts of this repository are published to Github Packages. To install them, you'll need to configure `~/.m2/settings.xml` with your Github's [personal access token](https://github.com/settings/tokens):

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
