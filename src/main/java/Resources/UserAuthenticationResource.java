package Resources;

import BusinessLogic.AccountManager;
import BusinessLogic.Models.Person;
import interfaces.KeyGenerator;
import interfaces.Parsabale;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAuthenticationResource {

    @Context
    UriInfo uriInfo;

    @EJB
    private AccountManager accountManager;

    @Inject
    private Parsabale parser;

    @Inject
    private KeyGenerator keyGenerator;

    @Getter
    private static Person currentPerson = null;

    @POST
    @Path("/authenticate")
    public Response login(String content) throws UnsupportedEncodingException {
        Map<String, String> authorizationParams = parser.extractParams(content);

        String username = authorizationParams.get("username");
        String password = authorizationParams.get("password");

        JSONObject jsonObjectResponse = new JSONObject();

        if (accountManager.checkPassword(username, password)) {
            currentPerson = accountManager.findByUsername(username);

            String token = issueToken(username);

            jsonObjectResponse.put("loginStatus", "OK");
            jsonObjectResponse.put("username", username);
            jsonObjectResponse.put("token", "Bearer " + token);
        } else {
            jsonObjectResponse.put("error", "Username or password is incorrect");
        }

        return Response.ok().entity(jsonObjectResponse).build();
    }

    @POST
    @Path("/register")
    public Response register(Person person) {

        JSONObject jsonObjectResponse = new JSONObject();

        if (accountManager.checkOnDublicate(person)) {
            accountManager.addUser(person);
            jsonObjectResponse.put("registerStatus", "OK");
        } else {
            jsonObjectResponse.put("error", "Username " + person.getUsername() + " is already in use!");
        }

        return Response.ok().entity(jsonObjectResponse).build();
    }
//
//    @POST
//    @Path("/check")
//    @JWTTokenNeeded
//    public Response checkHit(Hit hit) {
//        JSONObject jsonObjectResponse = new JSONObject();
//
//        if (hit.getR() <= 0) {
//            jsonObjectResponse.put("error", "Radius is not positive!");
//        } else {
//            jsonObjectResponse.put("x", hit.getX());
//            jsonObjectResponse.put("y", hit.getY());
//            jsonObjectResponse.put("r", hit.getR());
//
//            if (accountManager.checkHit(hit.getX(), hit.getY(), hit.getR(), currentPerson)) {
//                jsonObjectResponse.put("isInArea", "Yes");
//            } else {
//                jsonObjectResponse.put("isInArea", "No");
//            }
//        }
//        return Response.ok().entity(jsonObjectResponse).build();
//    }
//
//    @GET
//    @Path("/hits")
//    public Response allHits() {
//        List<Hit> all = this.accountManager.findAllUsersHits(currentPerson);
//
//        if (all == null || all.isEmpty()) {
//            return Response.noContent().build();
//        }
//
//        JsonArray data = all.stream()
//                .map(this::toJson)
//                .collect(
//                        Json::createArrayBuilder,
//                        JsonArrayBuilder::add,
//                        JsonArrayBuilder::add
//                ).build();
//
//        return Response.ok().entity(data).build();
//    }

    private String issueToken(String login) throws UnsupportedEncodingException {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jwtToken;
    }
//
//    private JsonObject toJson(Hit hit) {
//        return Json.createObjectBuilder()
//                .add("x", hit.getX())
//                .add("y", hit.getY())
//                .add("r", hit.getR())
//                .add("isInArea", hit.getIsInArea()
//                ).build();
//    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}