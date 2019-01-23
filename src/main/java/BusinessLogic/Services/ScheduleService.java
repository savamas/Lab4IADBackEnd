package BusinessLogic.Services;

import javax.ejb.*;


import javax.inject.Inject;
import javax.inject.Singleton;
import BusinessLogic.Models.OrderEnt;
import BusinessLogic.Models.OrderItem;
import BusinessLogic.Services.MailService;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


@Singleton
public class ScheduleService {
    MailService mailService = new MailService();
    ItemService itemService = new ItemService();


    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    @Schedule
    public void sendItemRequest(){
        mailService.sendItemRequest();
        itemService.flushRequestedItems();

    }

    @Schedule
    public void sendRentReminders(){

        LinkedList orders = itemService.getAllUnfinishedOrders();
        for (int i = 0; i<orders.size(); i++){

            OrderEnt order = (OrderEnt) orders.get(i);
            LinkedList<OrderItem> contents = (LinkedList<OrderItem>)order.getOrderItemCollection();
            StringBuilder stringBuilder = new StringBuilder();

            for (int j = 0; j<contents.size(); i++){
                OrderItem item = contents.get(j);
                Date curDate = new Date();
                if((curDate.after(addDays(order.getDateReceived(),item.getRentDuration())))){
                 stringBuilder.append(item.getItemType().getName()).append(": Аренда просрочена, дата окончания аренды:").append(addDays(order.getDateReceived(),item.getRentDuration()).toString()) .append("\n");
                }
            }

            String text = stringBuilder.toString();
            String topic = "В вашем заказе №"+ order.getId()+ " превышен срок аренды\n";
            String targetEmail = order.getCustomer().getUsername();

            mailService.sendMessage(topic,text,targetEmail);

        }
    }



}
