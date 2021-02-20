# KeeAuth Library
![Status](https://img.shields.io/badge/status-beta-bf9b1b)
[![Last commit](https://img.shields.io/github/last-commit/Jamsek-m/kee-auth/feature/v2)](https://github.com/Jamsek-m/kee-auth/commits/master)
[![GitHub license](https://img.shields.io/github/license/Jamsek-m/kee-auth)](https://github.com/Jamsek-m/kee-auth/blob/master/LICENSE)
> Library providing OpenId Connect security for KumuluzEE framework

#### Version 1.x.x
If you are looking for version `1.x.x` of KeeAuth, go to [1.x.x branch](https://github.com/Jamsek-m/kee-auth/tree/archive/1.x.x)


## Requirements

Library is compatible with Java 11+

## Usage

> Project is still in beta. See [beta projects](#beta-projects).

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
If your token claims are not mapped according to OIDC specification, you can provide your own mappings for AuthContext:

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

To enable security in resource class, you must annotate it with `@SecureResource`. Then you can annotate methods in this class with appropriate annotations. You can also put annotations on class. This means that non-annotated methods will take class-based access level.

```java
// Enable security in this class
@SecureResource
// All methods in this class need user to be authenticated (optional, you can put annotations on methods only)
@AuthenticatedAllowed
@RequestScoped
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleResource {
    
    @GET
    // only admins or salesmen can retrieve list of customers
    @RolesAllowed({"salesman", "admin"})
    public Response getCustomers() {
        // ... 
        return Response.ok(customers).build();
    }
    
    @GET
    // This method uses class annotated access level - authentication only
    public Response getCustomerDetails() {
        // ... 
        return Response.ok(customerDetails).build();
    }
    
    @POST
    // This method overrides class based annotation and is public - no authentication required
    @PublicResource
    public Response notifyCustomer() {
        // ... 
        return Response.ok().build();
    }
    
}
```

### Annotation types
* `@AuthenticatedAllowed`: to access this method a user (any valid user) must present valid JWT
* `@RolesAllowed({"dev"})`: to access this method a user must have role 'dev' [1]
* `@RolesAllowed(value = {"dev"}, clientName = "test-service")`: to access this method a user must have role 'dev' on a specified client (if identity provider supports it) [1]

If you want to expose single method in otherwise protected resource class you
can use `@PublicResource` annotation on method, you want to make public.

[1]: More in [resolvers](#roles-resolvers).

## Roles resolvers
Roles are resolved using role resolver. Library comes pre-packaged with default implementation of role resolver, however, you can also use another provided resolver, or write your own. 

[Read more](/resolvers/README.md)

## Security context

### Security context

You can retrieve data about user trying to access endpoint by injecting `AuthContext` object:

```java
@Inject
private AuthContext authContext;
```

Alternatively, you can also retrieve raw token:

```java
@Inject
@Token
private String rawToken;
```

In unsecured (public) endpoints, authContext will not be available.
Therefore, it is advisable to check if user is authenticated before
using its methods:
```java
if (authContext.isAuthenticated()) {
    // ...
}
``` 


Auth context provides following data:

* user id *(sub claim)*
* username *(preferred_username claim)*
* email *(email claim)*
* scopes *(scope claim)*
* authenticated flag
* other claims from token
* raw token

### Creating context manually

If for some reason, you cannot use CDI, you can also manually construct `AuthContext`:

```java
String jwt = "..."; // JWT from request
AuthContext context = ContextProducer.produceContext(jwt);
```

If for some reason, context cannot be constructed (i.e. expired jwt), it will return empty context (authenticated flag is set to false).

## Client credentials flow

KeeAuth provides client for performing calls with client credentials flow.

### Configuration
To configure client, you need to provide following configuration:
```yaml
kee-auth:
  oidc:
    client-credentials:
      client-id: test-client
      client-secret: e7820cf7-fbfb-4397-b156-91b15a2e3
      # Can be omitted if autoconfiguration is enabled
      token-url: https://keycloak.example.com/auth/realms/test-realm/protocol/openid-connect/token
```

### Usage

To use client for client credentials flow, use `IdentityProviderClient`, where you provide `Function` that receives one argument (that is access token) and returns retrieved data.

```java
public List<User> getAccounts() {
    String serviceCallUrl = "https://keycloak.example.com/auth/admin/realms/test-realm/users";
    
    return IdentityProviderClient.call(token -> {
        Response response = ClientBuilder.newClient()
            .target(serviceCallUrl)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .get();
            
        if (response.getStatus() >= 400) {
            String error = response.readEntity(String.class);
            LOG.error(error);
            throw new ServerErrorException(500);
        } else {
            return response.readEntity(new GenericType<List<User>>() {});
        }
    });
}
```

## Sample

Samples can be found on [this page](https://github.com/Jamsek-m/examples/tree/master/kee-auth)

## Changelog

Changes can be viewed on [releases page](https://github.com/Jamsek-m/kee-auth/releases) on GitHub.

## License
MIT

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

No credentials are required.