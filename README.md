# KeeAuth Library
[![Last commit](https://img.shields.io/github/last-commit/Jamsek-m/kee-auth/feature/v2)](https://github.com/Jamsek-m/kee-auth/commits/master)
[![GitHub license](https://img.shields.io/github/license/Jamsek-m/kee-auth)](https://github.com/Jamsek-m/kee-auth/blob/master/LICENSE)
> Library providing OpenId Connect Security for KumuluzEE framework

## Requirements

Library is compatible with Java 11+

## Usage

> Project is still in beta. [See](#beta-projects).

Import library in your project:
```xml
<dependency>
    <groupId>com.mjamsek.auth</groupId>
    <artifactId>kee-auth</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

## Configuration

### Client mapping
You can provide optional client mapping to decouple client id with code.

```yaml
kee-auth:
  clients:
    client-name-1: client-id-1
    client-name-2: client-id-2
```

### OpenId Connect

#### Autoconfiguration
Library can use provider's `well-known` endpoint to read configuration or retrieve keys.

```yaml
kee-auth:
  oidc:
    auto-configure: true
    well-known-url: https://keycloak.example.com/auth/realms/test-realm/.well-known/openid-configuration
```

You can also manage retrieving jwks from `jwks_url`, by completely disabling fetching from a remote source:

```yaml
kee-auth:
  oidc:
    use-jwks: false
```
or by providing own `jwks_url`:
```yaml
kee-auth:
  oidc:
    use-jwks: true
    jwks-url: https://keycloak.example.com/auth/realms/test-realm/protocol/openid-connect/certs
```

### Json Web Token

#### Token time leeway

You can configure default time leeway (1 second), by setting key:
```yaml
kee-auth:
  jwt:
    time-leeway: 2
```

#### Token claims mapping
If your claims are not mapped by OIDC specifications, you can provide your own mappings for AuthContext:

```yaml
kee-auth:
  jwt:
    claims:
      id: sub
      email: email
      username: preferred_username
      scope: scope
```

#### Token keys

Keys if not retrieved from JWKS endpoint need to be specified in configuration:
```yaml
kee-auth:
  jwt:
    keys:
      - kid: g7j
        alg: HS256
        secret: secretkey
      - kid: d5i
        alg: RS256
        n: 'qtzjj3KuFldrDL3rpIsKoEjzxwHQEUmzQNHoCP8CYiA-29q-Tqt8s0ALg5ruK-UH5pBJxtixEuYG0jkNcHFLOfJz45W2Lc1kjYtOQbjMEDewrCG2pDDT-kBbjs_GaaOINQfV5e1Duv9BYpHSaQtrin-yeOX4Lu5U6qTRzuSo-bO29dB6Vr5Lj8vhEUm5vQE9RvsM1ZyXwGV6LLwi-cerfEhKgHhfoUzGuZqB4EI74LoyPytxTafb53tSz2eccALjTdfor03NUae89qiOC11FGw0gVteoplkW7nH6dVMwfTfByGb6fTzYy2Yx_30kCO6yNwALQdDujJNxs2Y332w'
        e: 'ATEF'
      - kid: 7e9
        alg: RS256
        x5c: 'MIIClTCCAX0CBgF3ISs0kjANBgkqhkiG9w0BAQsFADAOMQwwCgYDVQQDDANrcnYwHhcNMjEwMTIwMTg1ODM1WhcNMzEwMTIwMTkwMDE1WjAOMQwwCgYDVQQDDANrcnYwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCq3OOPcq4WV2sMveukiwqgSPPHAdARSbNA0egI/wJiID7b2r5Oq3yzQAuDmu4r5QfmkEnG2LES5gbSOQ1wcUs58nPjlbYtzWSNi05BuMwQN7CsIbakMNP6QFuOz8Zpo4g1B9Xl7UO6/0FikdJpC2uKf7J45fgu7lTqpNHO5Kj5s7b10HpWvkuPy+ERSbm9AT1Ha+wzVnJfAZXosvCL5w4VFl8SEqAeF+hTMa5moHgQjvgujI/K3FMOC4De1LPZ5xwAuNN1+ivTc1RIJ32qI4LXUUbDSBW16imWRbucfp1UzB9N8HIZvp9PNjLZjH/fSQI7rI3AAtB0O6Mk3GzZiffbAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAKAT+e2wVNEJld5CCtpqN13fvCJw7Yc2HZdeg648JqkrLOVyZltynMA2VG6NM4sDwQcH3Cb75nPcfND+rUrTSDiu0eQ3xfCh4pfsqvh8EEQ95yrGGm91McdGlNt24IUZNfGFzGZs/cCfuxoQUfpMCSSlVN/6SFpm8E5wHMYP9ALR+Aw8eVWMOOpDDzQWDZHo7lxkz4rMuaeWqOggAsdsrEblpWoU52Mxy02x+I8GUJdxjwL1atRI1yuUp7LkZ1O+NsdedOvHpUlDGRPIbF2VfeurrIIs0y2Pz1TdCR5eSxjVHPleiabaKjWft8qUysGGptTr+FmGj9KGad4L8='
```
Currently only **Hmac with SHA-1** (HS256, HS384, HS512) and **RSA** (RS256, RS384, RS512) are supported algorithms for token verification.

## Authentication and authorization


## Old version
[here](https://github.com/Jamsek-m/kee-auth/tree/archive/1.x.x)

## Beta projects
For beta projects you need to include repository in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>mjamsek-beta</id>
        <url>https://nexus.mjamsek.com/repository/mjamsek-beta</url>
    </repository>
</repositories>
```