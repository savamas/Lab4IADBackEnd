package Resources;

import BusinessLogic.AccountManager;
import BusinessLogic.Models.*;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.*;
import com.sun.deploy.net.HttpRequest;
import interfaces.KeyGenerator;
import interfaces.Parsabale;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Context
    UriInfo uriInfo;

    @EJB
    private OrderService orderService;

    @Inject
    private ItemService itemService;

    @Inject
    private MailService mailService;

    @Inject
    private Parsabale parser;

    @POST
    @Path("/setStatus")
    public Response setStatuses(String content){
        Map<String, String> statusValues = parser.extractParams(content);
        String newDeliveryStatus = statusValues.get("newDeliveryStatus");
        String newPaymentStatus = statusValues.get("newDeliveryStatus");
        int orderId = Integer.parseInt(statusValues.get("orderId"));

        OrderEnt ord = orderService.findOrder(orderId);

        boolean needUpdates = false;
        if (ord.getDeliveryStatus().equals(newDeliveryStatus)){
            orderService.setDeliveryStatus(newDeliveryStatus, ord);
            needUpdates = true;
        }

        if (ord.getPaymentStatus().equals(newPaymentStatus)){
            orderService.setPaymentStatus(newPaymentStatus, ord);
            needUpdates = true;
        }

        if(needUpdates)
            mailService.sendUpdate(ord, newDeliveryStatus, newPaymentStatus);

        return Response.ok().build();
    }

    @POST
    @Path("/create")
    public Response createOrder(String content, @Context HttpSession session) {
        JsonParser jparser = new JsonParser();
        JsonElement root = jparser.parse(content);
        JsonObject jobj = root.getAsJsonObject();
        JsonArray items = jobj.getAsJsonArray("items");

        Map<String, String> statusValues = parser.extractParams(content);
        String paymentType = statusValues.get("paymentType");
        String paymentStatus = statusValues.get("paymentStatus");
        String deliveryType = statusValues.get("deliveryType");





        LinkedList cartItems = (LinkedList<OrderItem>)session.getAttribute("items");
        LinkedList<Integer> ids = new LinkedList();
        for (Object item : cartItems) {
            OrderItem orItem = (OrderItem)item;
            ids.add(orItem.getId());
        }

        Map<Integer,Integer> map = itemService.getItemsAmountByIds(ids);



        for (int i = 0 ; i < ids.size(); i++){

            OrderItem checkItem = (OrderItem)cartItems.get(i);
            if (checkItem.getAmount() > map.get(checkItem.getId())){
                itemService.addRequestedItem(checkItem, (checkItem.getAmount()- map.get(checkItem.getId())));
            }
        }

        orderService.addOrder((Person)UserAuthenticationResource.getCurrentPerson(), cartItems,paymentType,paymentStatus, deliveryType);
        return Response.ok().build();
    }

    @POST
    @Path("/addToCart")
    public Response setStatuses(String content, @Context HttpServletRequest request){

//
//        content ="{\"id\":\"11\",\"amount\":\"1\",\"booking\":\"\"}";

        HttpSession session = request.getSession();

        if (session.getAttribute("items") == null){
            Collection<OrderItem> items = new LinkedList<>();
            session.setAttribute("items", items);

        }
        Gson gson = new Gson();


        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement id = obj.get("id");
        JsonElement amount = obj.get("amount");
        JsonElement date = obj.get("booking");

        OrderItem addition = new OrderItem();
        addition.setItemType(itemService.getItemTypeById(id.getAsInt()));
        addition.setAmount(amount.getAsInt());
        String oldDate = date.getAsString();
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date newDate = new Date();
        if((oldDate.length() == 0)||(oldDate == null)){
            oldDate = "01/01/1970";
        }


try {
    newDate = oldDateFormat.parse(oldDate);
}catch (Exception e){ }

        addition.setStartDate(newDate);
        List newItems = (LinkedList<OrderItem>)session.getAttribute("items");
        newItems.add(addition);

        session.setAttribute("items",newItems);
        return Response.ok().entity(((LinkedList<OrderItem>) session.getAttribute("items")).size()).build();
    }




    @GET
    @Path("/getCartItems")
    public Response getCartItems(String content, @Context HttpServletRequest request){

        HttpSession session = request.getSession();
        Gson gson = new Gson();

        if (session.isNew()){
            return Response.ok().entity(gson.toJson("No Items")).build();
        }

        List <OrderItem> testItems = new LinkedList<>();
        testItems = (List <OrderItem>)session.getAttribute("items");
        if (testItems == null){
            return Response.ok().entity(gson.toJson("No Items")).build();
        }



        List<OrderItem> items = (List<OrderItem>)session.getAttribute("items");
        List<OrderItemDTO> dtos = new LinkedList<>();
        for (OrderItem item : items) {
            OrderItemDTO dto = new OrderItemDTO();
            Calendar cal = Calendar.getInstance();
            cal.setTime(item.getStartDate());
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dayString = String.valueOf(day);
            if(day<10)
                dayString ="0"+day;
            String string = month + "/" + dayString + "/" + year;
            if(year == 1970)
            {
                string = "";
            }
            dto.setAmount(item.getAmount());
            dto.setDate(string);
            ItemType iType = itemService.getItemTypeById(item.getItemType().getId());
            dto.setName(iType.getName());
            dto.setPrice(iType.getPrice());
            dto.setUrl(iType.getImageUrl());
            dto.setCategoryId(iType.getCategory().getId());
            dtos.add(dto);
        }
        return Response.ok().entity(gson.toJson(dtos)).build();
    }

    @POST
    @Path("/removeFromCart")
    public Response setStatuses(String content, @Context HttpSession session){

        Map<String, String> statusValues = parser.extractParams(content);
        int id = Integer.valueOf(statusValues.get("id"));
        LinkedList items = (LinkedList<OrderItem>)session.getAttribute("items");
        for(int i =0; i<items.size(); i++){
            OrderItem o = (OrderItem)items.get(i);
            if(o.getId() == id){
                items.remove(i);
            }

            session.setAttribute("items",items);
        }
        return Response.ok().build();
    }







}