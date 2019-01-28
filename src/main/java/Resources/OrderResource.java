package Resources;

import BusinessLogic.AccountManager;
import BusinessLogic.Models.*;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.*;
import interfaces.KeyGenerator;
import interfaces.Parsabale;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @EJB
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
    public Response setStatuses(String content, @Context HttpServletRequest request, @Context HttpSession session){
        if (session != null){
            session = request.getSession();
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
try {


    newDate = oldDateFormat.parse(oldDate);
}catch (Exception e){

}
        addition.setStartDate(newDate);
        LinkedList newItems = (LinkedList<OrderItem>)session.getAttribute("items");
        newItems.add(addition);
        session.setAttribute("items",newItems);
        return Response.ok().build();
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