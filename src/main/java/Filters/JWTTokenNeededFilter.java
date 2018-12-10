package Filters;

import interfaces.JWTTokenNeeded;
import interfaces.KeyGenerator;
import interfaces.Priority;
import io.jsonwebtoken.Jwts;

import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Key;

@Provider
@JWTTokenNeeded
@Priority(Priorities.AUTHENTICATION)
public class JWTTokenNeededFilter implements ContainerRequestFilter {

    @Inject
    private KeyGenerator keyGenerator;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            Key key = this.keyGenerator.generateKey();
         //  Key key = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes("UTF-8");
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            System.out.println("Valid token: " + token);
        } catch (Exception e) {
            System.out.println("Invalid token: " + token);
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

        }
    }
}