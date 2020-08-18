# KeeAuth Library

[![Maven Central](https://img.shields.io/maven-central/v/com.mjamsek.auth/kee-auth)](https://mvnrepository.com/artifact/com.mjamsek.auth/kee-auth)
[![Last commit](https://img.shields.io/github/last-commit/Jamsek-m/kee-auth/master)](https://github.com/Jamsek-m/kee-auth/commits/master)
![Build Status](https://jenkins.mjamsek.com/buildStatus/icon?job=kumuluzee-keycloak-integration-lib)
[![GitHub license](https://img.shields.io/github/license/Jamsek-m/kee-auth)](https://github.com/Jamsek-m/kee-auth/blob/master/LICENSE)

> Library providing Keycloak Security for KumuluzEE framework

## Requirements

Library is compatible with Java 11+ and Keycloak 7.0.0+

## Usage

Import library in your project:
```xml
<dependency>
    <groupId>com.mjamsek.auth</groupId>
    <artifactId>kee-auth</artifactId>
    <version>${kee-auth.version}</version>
</dependency>
``` 

Provide configuration values in `config.yml`:

```yaml
keycloak:
  # Mandatory options:
  realm: keycloak-realm
  auth-server-url: https://keycloak.example.com/auth
  client-id: keycloak-client
  auth:
    # Confidential clients need this to perform service calls
    client-secret: <client_secret>
    # Leeway when verifying token (default 1000)
    token-leeway: 1000
    # Provide public certificate for token verification with RS265.
    # If it is not provided, it will be fetched from Keycloak's well-known endpoint.
    cert: <cert>
```

### Authentication and authorization

To enable security in resource class, you must annotate it with `@SecureResource`.
Then you can annotate methods in this class with appropriate annotations.
You can also put annotations on class. This means that non-annotated methods 
will take class-based access level.

```java
// enable security in this class
@SecureResource
// all methods need user to be authenticated (optional, you can put annotations on method only)
@AuthenticatedAllowed
@RequestScoped
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SampleResource {

    @GET
    // only admins or salesmen can retrieve list of customers
    @RealmRolesAllowed({"salesman", "admin"})
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

#### Annotation types:

* `@AuthenticatedAllowed`: to access this method a user must present valid JWT
* `@RolesAllowed({"dev"})`: to access this method a user must have role 'dev' (either in realm or on any client)
* `@RealmRolesAllowed({"dev"})`: to access this method a user must have **realm** role 'dev'
* `@ClientRolesAllowed(client = "keycloak-client", roles = {"dev"})`: to access this method a user must have **client** role 'dev' on a client 'keycloak-client'.

If you want to expose single method in otherwise protected resource class you 
can use `@PublicResource` annotation on method, you want to make public.

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
Therefore, it is good practice to check if user is authenticated before 
using its methods:
```java
if (authContext.isAuthenticated()) {
    // ...
}
``` 

Auth context provides following data: 

* user id *(token subject)*
* username
* email
* realm roles
* client roles
* scopes
* authenticated flag
* other claims from token
* raw token

### Keycloak client

Library also provides a client to perform service calls to keycloak server.

To use it, configuration key `keycloak.auth.client-secret` must be provided.
Additionally, configured client must be **confidential** and service account 
must be **enabled** (with appropriate roles assigned).

When you have configured service properly, you can call keycloak
using `KeycloakClient` class:

```java
KeycloakClient.callKeycloak((token) -> {
    // perform http call to keycloak using token variable as credential
    // note that provided token belongs to service account
});
```

`callKeycloak` method accepts lambda function with one string parameter.
This parameter is set by keycloak client to service token, which it is able 
to retrieve on its own using client secret we provided.

Library requires `kumuluzee-rest-client` dependency to be provided at runtime.
It is therefore very advisable that you use rest client yourself when using 
`callKeycloak` function.

```java
// KeycloakAPI.java
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface KeycloakAPI {
   
    @GET
    @Path("/admin/realms/{realm}/users")
    List<Account> getAccounts(
        @PathParam("realm") String realm,
        @HeaderParam("Authorization") String authorizationHeader
    );
}
``` 

```java
// AccountService.java
public class AccountService {

    public List<Account> getAccountsFromKeycloak() {

        String realm = ConfigurationUtil.getInstance().get("keycloak.realm").get();
        String keycloakUrl = ConfigurationUtil.getInstance().get("keycloak.auth-server-url").get();

        KeycloakAPI api = RestClientBuilder
            .newBuilder()
            .baseUri(URI.create(keycloakUrl))
            .build(KeycloakAPI.class);

        List<Account> accounts = KeycloakClient.callKeycloak((token) -> {
            return api.getAccounts(realm, "Bearer " + token);
        });
        return accounts;
    }
}
```

## Sample

Sample can be found on [this page](https://github.com/Jamsek-m/examples/tree/master/javaee/kumuluzee/kumuluzee-keycloak-integration-lib-sample)

## Known issues


### 401 response

On 401 response, Jetty throws ProcessingException, due to which response is not processed by registered mappers.


## Changelog

Changes can be viewed on [releases page](https://github.com/Jamsek-m/kumuluzee-keycloak-integration-lib/releases) on GitHub.

## License

MIT
