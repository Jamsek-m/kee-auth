# KeeAuth role resolvers

To parse roles from claims, a role resolver is needed, to parse claims, specific for used identity provider.

All registered resolvers are scanned at runtime and are invoked in order according to their priority (higher the number, higher priority), until one of them returns valid set of roles.

To disable specific resolver, set configuration key `kee-auth.role-resolvers.<resolver-id>.enabled` to `false`.

## Default role resolver
> Id: default
> Priority: 0

Expects claim to be of type array of strings. Claim name is `roles` by default, but can be configured by setting key `kee-auth.role-resolvers.default.roles-mapping`.

## Keycloak role resolver
> Id: keycloak
> Priority: 200

To use Keycloak role resolver, you need to include the following in your `pom.xml`:
```xml
<dependency>
    <groupId>com.mjamsek.auth.resolvers</groupId>
    <artifactId>kee-auth-keycloak-resolver</artifactId>
    <version>2.0.0-SNAPSHOT</version>
</dependency>
```

Keycloak resolver is able to read both realm and client roles:

* For realm roles, simply omit `clientName` param in `@RolesAllowed` annotation
* For client roles, provide `clientName` param in `@RolesAllowed` annotation and resolver will return user's roles belonging to client with id mapped to provided `clientName`, or client with id same as `clientName` param if no mapping was provided (more [here](../README.md#client-mapping)).

## Implementing own role resolver

To provide your own role resolver, all you need to do is:
* implement class `com.mjamsek.auth.common.resolvers.RolesResolver`,
* annotate it with `@ResolverDef`, where you provide resolver id and
* in `src/main/resources/META-INF/services` create file with name `com.mjamsek.auth.common.resolvers.RolesResolver` and put reference to your implementation as file content (example: `com.example.auth.resolvers.MyCustomResolver`).