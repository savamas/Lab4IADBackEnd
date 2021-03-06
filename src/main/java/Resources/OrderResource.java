package Resources;

import BusinessLogic.Models.ItemType;
import BusinessLogic.Models.OrderEnt;
import BusinessLogic.Models.OrderItem;
import BusinessLogic.Models.Person;
import BusinessLogic.Services.ItemService;
import BusinessLogic.Services.MailService;
import BusinessLogic.Services.OrderService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import interfaces.JWTTokenNeeded;
import interfaces.Parsabale;

import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.text.SimpleDateFormat;
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
    @JWTTokenNeeded
    @Path("/setStatus")
    public Response setStatuses(String content, @Context HttpServletRequest request){
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();

        JsonElement newDeliveryStatusJ = obj.get("newDeliveryStatus");
        JsonElement newPaymentStatusJ = obj.get("newPaymentStatus");
        JsonElement orderIdJ = obj.get("orderId");

        String newDeliveryStatus = newDeliveryStatusJ.getAsString();
        String newPaymentStatus = newPaymentStatusJ.getAsString();
        int orderId = orderIdJ.getAsInt();

        OrderEnt ord = orderService.findOrder(orderId);

        boolean needUpdates = false;
        if (!(ord.getDeliveryStatus().equals(newDeliveryStatus))){
            orderService.setDeliveryStatus(newDeliveryStatus, ord);
            needUpdates = true;
        }

        if (!(ord.getPaymentStatus().equals(newPaymentStatus))){
            orderService.setPaymentStatus(newPaymentStatus, ord);
            needUpdates = true;
        }


        //if(needUpdates)
          //  mailService.sendUpdate(ord, newDeliveryStatus, newPaymentStatus, request);

        return Response.ok().build();
    }

    @POST
    @Path("/create")
    @JWTTokenNeeded
    public Response createOrder(String content, @Context HttpServletRequest request) {


        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement paymentType = obj.get("paymentType");
        JsonElement paymentStatus = obj.get("paymentStatus");
        JsonElement deliveryType = obj.get("deliveryType");


        HttpSession session = request.getSession();

        if (session.getAttribute("items") == null)
            return Response.ok().entity(gson.toJson("No Items")).build();






        LinkedList<OrderItem> cartItems = (LinkedList<OrderItem>)session.getAttribute("items");
        LinkedList<Integer> ids = new LinkedList();
        for (OrderItem item : cartItems) {

            ids.add(item.getItemType().getId());
        }

        Map<Integer,Integer> map = itemService.getItemsAmountByIds(ids);


        for (OrderItem checkItem: cartItems) {



            if (checkItem.getAmount() > map.get(checkItem.getItemType().getId())){
                itemService.addRequestedItem(checkItem, (checkItem.getAmount()- map.get(checkItem.getId())));
            }
            checkItem.getItemType().setPropertyCollection(itemService.getPropertiesByItemTypeId(checkItem.getItemType().getId()));
        }

        orderService.addOrder((Person) UserAuthenticationResource.getCurrentPerson(), cartItems,paymentType.getAsString(),paymentStatus.getAsString(),deliveryType.getAsString());
        List<OrderItem> newList = new LinkedList<>();
        session.setAttribute("items", newList);
        return Response.ok().build();
    }

    @POST
    @Path("/addToCart")
    public Response addToCart(String content, @Context HttpServletRequest request){



        //content = "{\"id\":\"11\",\"amount\":\"1\",\"booking\":\"\"}";

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

        List<OrderItem> checkItems = orderService.getBookedLocations();
        for (OrderItem checkItem: checkItems) {
            if(checkItem.getStartDate().equals(newDate))
                return Response.ok().entity(gson.toJson("Date already booked")).build();
        }

        addition.setStartDate(newDate);
        List<OrderItem> newItems = (LinkedList<OrderItem>)session.getAttribute("items");

        OrderItem toRemove = new OrderItem();
        for ( OrderItem item: newItems) {
            if(item.getItemType().getName().equals(addition.getItemType().getName())){
                addition.setAmount(addition.getAmount()+item.getAmount());
                toRemove = item;

            }
        }
        newItems.remove(toRemove);
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
        if ((testItems == null)||(testItems.size() == 0)){
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
    public Response removeFromCart(String content, @Context HttpServletRequest request){






        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement name = obj.get("name");


        HttpSession session = request.getSession();

        List<OrderItem> items = (List<OrderItem>) session.getAttribute("items");


        List<OrderItem> toRemove = new LinkedList<>();
        for (OrderItem item : items) {
            if(item.getItemType().getName().equals(name.getAsString()))
                toRemove.add(item);
        }

        items.removeAll(toRemove);
        session.setAttribute("items", items);

        return Response.ok().build();
    }


    @GET
    @Path("/getPersonOrders")
    public Response getPersonOrders(@Context HttpServletRequest request) {

        List<OrderEnt> orders = (List<OrderEnt>) orderService.getCustomerOrders(UserAuthenticationResource.getCurrentPerson());

        List<OrderDTO> resultList = new LinkedList<>();
        resultList.clear();

        Gson gson = new Gson();

        for (OrderEnt orderEnt: orders) {

            SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(orderEnt.getId());
            Optional<Date> dateCrOpt = Optional.of(orderEnt.getDateCreated());
            Optional<Date> dateRcOpt = Optional.of(orderEnt.getDateCreated());

            if (!dateCrOpt.isPresent())
                orderDTO.setDateCreated("Не указано");
            else
                orderDTO.setDateCreated(oldDateFormat.format(orderEnt.getDateCreated()));

            if(!dateRcOpt.isPresent()||orderEnt.getDateReceived()==null)
                orderDTO.setDateReceived("Не известно");
            else{

                orderDTO.setDateReceived(oldDateFormat.format(orderEnt.getDateReceived()));
            }

            orderDTO.setPaymentStatus(orderEnt.getPaymentStatus());
            orderDTO.setPaymentType(orderEnt.getPaymentType());
            orderDTO.setDeliveryStatus(orderEnt.getDeliveryStatus());
            orderDTO.setDeliveryType(orderEnt.getDeliveryType());
            if(orderEnt.getStatus() == null)
            {
                orderDTO.setStatus("Не указан");
            }
            else
            {
                orderDTO.setStatus(orderEnt.getStatus());
            }

            resultList.add(orderDTO);
        }

        return Response.ok().entity(gson.toJson(resultList)).build();
    }


    @POST
    @Path("/getOrderItems")
    public Response getOrderItems (String content, @Context HttpServletRequest request) {


        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement id = obj.get("id");

        List<OrderItem> items = (List<OrderItem>) orderService.getOrderContents(id.getAsInt());

        List<OrderItemDTO> resultList = new LinkedList<>();

        Gson gson = new Gson();

        for (OrderItem orderItem : items) {

            OrderItemDTO itemDTO = new OrderItemDTO();

            itemDTO.setPrice(orderItem.getItemType().getPrice());
            itemDTO.setName(orderItem.getItemType().getName());
            Calendar cal = Calendar.getInstance();
            cal.setTime(orderItem.getStartDate());
            int month = cal.get(Calendar.MONTH) + 1;
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String dayString = String.valueOf(day);
            if(day<10)
                dayString ="0"+day;
            String string = month + "/" + dayString + "/" + year;

            itemDTO.setDate(string);
            itemDTO.setCategoryId(orderItem.getItemType().getCategory().getId());
            itemDTO.setUrl(orderItem.getItemType().getImageUrl());
            itemDTO.setAmount(orderItem.getAmount());

            resultList.add(itemDTO);
        }

        return Response.ok().entity(gson.toJson(resultList)).build();
    }


    @GET
    @Path("/getPersonInfo")
    public Response getPersonInfo(@Context HttpServletRequest request) {

        PersonDTO personDTO = new PersonDTO();
        Person sourcePerson = UserAuthenticationResource.getCurrentPerson();

        personDTO.setFirstName(sourcePerson.getFirstName());
        personDTO.setLastName(sourcePerson.getLastName());
        personDTO.setPhoneNum(sourcePerson.getPhoneNum());
        personDTO.setUsername(sourcePerson.getUsername());
        personDTO.setSex(sourcePerson.getSex());

        Gson gson = new Gson();

        return Response.ok().entity(gson.toJson(personDTO)).build();
    }

    @GET

    @Path("/getUnclaimedOrders")
    public Response getUnclaimedOrders() {
        List<OrderEnt> orders = (List<OrderEnt>)orderService.getUnclaimedOrders();
        List<OrderDTO> resultList = new LinkedList<>();
        Gson gson = new Gson();

        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        for (OrderEnt orderEnt: orders) {

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(orderEnt.getId());
            orderDTO.setDateCreated(oldDateFormat.format(orderEnt.getDateCreated()));
            orderDTO.setPaymentStatus(orderEnt.getPaymentStatus());
            orderDTO.setPaymentType(orderEnt.getPaymentType());
            orderDTO.setDeliveryType(orderEnt.getDeliveryType());
            orderDTO.setDeliveryStatus(orderEnt.getDeliveryStatus());

            if(orderEnt.getStatus() == null)
            {
                orderDTO.setStatus("Не указан");
            }
            else
            {
                orderDTO.setStatus(orderEnt.getStatus());
            }

            resultList.add(orderDTO);
        }

        return Response.ok().entity(gson.toJson(resultList)).build();
    }

    @POST
    @JWTTokenNeeded
    @Path("/claimOrder")
    public Response claimOrder(String content) {

        //content =  "{\"id\":\"13\"}";
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement orderId = obj.get("id");

        orderService.claimOrder(UserAuthenticationResource.getCurrentPerson(),orderId.getAsInt());

        return Response.ok().build();

    }

    @GET
    @JWTTokenNeeded
    @Path("/getActiveOrders")
    public Response getActiveOrders() {
        List<OrderEnt> orders = (List<OrderEnt>) orderService.getActiveOrders(UserAuthenticationResource.getCurrentPerson().getId());

        List<OrderDTO> resultList = new LinkedList<>();
        resultList.clear();
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Gson gson = new Gson();

        for (OrderEnt orderEnt: orders) {

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(orderEnt.getId());
            orderDTO.setDateCreated(oldDateFormat.format(orderEnt.getDateCreated()));
            orderDTO.setPaymentStatus(orderEnt.getPaymentStatus());
            orderDTO.setPaymentType(orderEnt.getPaymentType());
            orderDTO.setDeliveryType(orderEnt.getDeliveryType());
            orderDTO.setPhoneNum(orderEnt.getCustomer().getPhoneNum());
            orderDTO.setDeliveryStatus(orderEnt.getDeliveryStatus());

            if(orderEnt.getStatus() == null)
            {
                orderDTO.setStatus("Не указан");
            }
            else
            {
                orderDTO.setStatus(orderEnt.getStatus());
            }

            resultList.add(orderDTO);
        }

        return Response.ok().entity(gson.toJson(resultList)).build();
    }

    @POST
    @Path("/getOrder")
    public Response getOrder (String content, @Context HttpServletRequest request) {

        OrderDTO orderDTO = new OrderDTO();

        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement elem = jsonParser.parse(content);
        JsonObject obj = elem.getAsJsonObject();
        JsonElement orderId = obj.get("id");

        OrderEnt orderEnt = orderService.findOrder(orderId.getAsInt());

        orderDTO.setPaymentStatus(orderEnt.getPaymentStatus());
        orderDTO.setPaymentType(orderEnt.getPaymentType());
        orderDTO.setDeliveryStatus(orderEnt.getDeliveryStatus());
        orderDTO.setDeliveryType(orderEnt.getDeliveryType());

        return Response.ok().entity(gson.toJson(orderDTO)).build();
    }






}