package Resources;

import BusinessLogic.Models.*;
import BusinessLogic.Services.FilterSetting;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import interfaces.Parsabale;
import org.omg.CORBA.Object;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.awt.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
        List<ItemType> items = itemService.getItemsByCategory(el.getAsInt());
        LinkedList<ItemTypeDTO> itemDTOs = new LinkedList<>();
        for (ItemType item: items) {
            ItemTypeDTO dto = new ItemTypeDTO();
            dto.setName(item.getName());
            dto.setUrl(item.getImageUrl());
            dto.setId(item.getId());
            dto.setPrice(item.getPrice());
            itemDTOs.add(dto);
        }
        return Response.ok().entity(gson.toJson(itemDTOs)).build();
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
        List<FilterSetting> filters;
        Type type = new TypeToken<List<FilterSetting>>(){}.getType();
        filters = gson.fromJson(fil, type);

        List<ItemType> items = itemService.getFilteredItems(cat.getAsInt(),filters);
        LinkedList<ItemTypeDTO> itemDTOs = new LinkedList<>();
        for (ItemType item: items) {
            ItemTypeDTO dto = new ItemTypeDTO();
            int i;
            dto.setName(item.getName());
            dto.setUrl(item.getImageUrl());
            dto.setId(item.getId());
            dto.setPrice(item.getPrice());
            itemDTOs.add(dto);
        }


        return Response.ok().entity(gson.toJson(itemDTOs)).build();
    }

    @POST
    @Path("/getSearchedItems")
    public Response getSearchedItems(String content){

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement el = obj.get("query");
        JsonElement cat= obj.get("category");

        List<ItemType> list = itemService.getSearchedItems(el.getAsString(), cat.getAsInt());
        LinkedList<ItemTypeDTO> itemDTOs = new LinkedList<>();
        for (ItemType item: list) {
            ItemTypeDTO dto = new ItemTypeDTO();
            dto.setName(item.getName());
            dto.setUrl(item.getImageUrl());
            dto.setId(item.getId());
            dto.setPrice(item.getPrice());
            itemDTOs.add(dto);
        }
        return Response.ok().entity(gson.toJson(itemDTOs)).build();
    }

    @POST
    @Path("/getItemById")
    public Response getItemById(String content) {

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement el = obj.get("id");

        ItemType item = itemService.getItemTypeById(el.getAsInt());

        ItemTypeDTO dto = new ItemTypeDTO();
        dto.setName(item.getName());
        dto.setUrl(item.getImageUrl());
        dto.setId(item.getId());
        dto.setPrice(item.getPrice());


        return Response.ok().entity(gson.toJson(dto)).build();
    }


}