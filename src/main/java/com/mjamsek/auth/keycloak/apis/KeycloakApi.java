package com.mjamsek.auth.keycloak.apis;

import com.mjamsek.auth.keycloak.config.KeycloakConfig;
import com.mjamsek.auth.keycloak.exceptions.KeycloakCallException;
import com.mjamsek.auth.keycloak.models.TokenResponse;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletionStage;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterProvider(AuthenticationExceptionMapper.class)
@RegisterProvider(GenericExceptionMapper.class)
public interface KeycloakApi {
    
    default String getServiceCredentials() {
        return KeycloakConfig.ServiceCall.getAuthHeader();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ClientHeaderParam(name = HttpHeaders.AUTHORIZATION, value = "{getServiceCredentials}")
    @Path("/realms/{realm}/protocol/openid-connect/token")
    TokenResponse getServiceToken(@PathParam("realm") String realm, Form form) throws KeycloakCallException;
    
    @GET
    @Path("/realms/{realm}/protocol/openid-connect/certs")
    @Deprecated
    JsonObject getCerts(@PathParam("realm") String realm);
    
    @GET
    @Path("/realms/{realm}/protocol/openid-connect/certs")
    CompletionStage<JsonObject> getCertsAsync(@PathParam("realm") String realm);
    
}
