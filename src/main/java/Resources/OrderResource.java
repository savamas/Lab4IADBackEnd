package Resources;

import BusinessLogic.AccountManager;
import BusinessLogic.Models.*;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interfaces.KeyGenerator;
import interfaces.Parsabale;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Context
    UriInfo uriInfo;

    @Inject
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
    public Response setStatuses(String content, @Context HttpServletRequest request, @Context HttpSession session){
        if (session != null){
            session = request.getSession();
            Collection<OrderItem> items = new LinkedList<>();
            session.setAttribute("items", items);
        }
        Map<String, String> statusValues = parser.extractParams(content);
        String id = statusValues.get("id");
        String amount = statusValues.get("amount");
        boolean rentStatus = Boolean.valueOf(statusValues.get("rentStatus"));
        int rentDuration = Integer.valueOf(statusValues.get("rentDuration"));

        OrderItem addition = new OrderItem();
        addition.setItemType(itemService.getItemTypeById(Integer.valueOf(id)));
        addition.setAmount(Integer.valueOf(amount));
        addition.setRentDuration(rentDuration);
        addition.setRentStatus(rentStatus);

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