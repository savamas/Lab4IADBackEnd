package Resources;

import BusinessLogic.Models.*;
import BusinessLogic.Services.FilterSetting;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.*;
import interfaces.Parsabale;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

@Path("/catalogue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatalogueResource {

    @Context
    UriInfo uriInfo;



    @Inject
    private OrderService orderService;

    @EJB
    private ItemService itemService;

    @Inject
    private MailService mailService;

    @Inject
    private Parsabale parser;

    @POST
    @Path("/getItems")
    public Response getItems(String content){
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement el = obj.get("category");
        LinkedList<ItemType> items = (LinkedList<ItemType>)itemService.getItemsByCategory(el.getAsInt());
        return Response.ok().entity(gson.toJson(items)).build();
    }

    @GET
    @Path("/getCategories")
    public  Response getCategories()
    {
        Gson gson = new Gson();


        return Response.ok().entity(gson.toJson(itemService.getDistinctCategories())).build();
    }

    @POST
    @Path("/getFilters")
    public Response getFilter(String content)
    {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement el = obj.get("category");

        LinkedList<FilterSetting> items = (LinkedList<FilterSetting>)itemService.getFiltersByCategoryId(el.getAsInt());
        return Response.ok().entity(gson.toJson(items)).build();
    }

    @POST
    @Path("/getFilteredItems")
    public Response getFilteredItems(String content){


        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement cat = obj.get("category");
        JsonElement fil = obj.get("filters");
        LinkedList<FilterSetting> filters = new LinkedList<>();
        filters = (LinkedList<FilterSetting>) gson.fromJson(fil,filters.getClass());
        LinkedList<ItemType> items = (LinkedList<ItemType>)itemService.getFilteredItems(cat.getAsInt(),filters);
        return Response.ok().entity(gson.toJson(items)).build();
    }
    

}