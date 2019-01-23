package BusinessLogic.Services;

import BusinessLogic.Models.*;
import BusinessLogic.Services.*;
import lombok.Data;

import javax.inject.Inject;
import javax.json.stream.JsonParser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



@Data
public class MailService {

    private  String senderUsername = "username@gmail.com";
    private  String senderPassword = "password";
    private  String distributorEmail = "distributor@gmail.com";
    Properties props;

    public void MailService(){


          props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    public void sendMessage(String topic, String text, String targetEmail) {


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderUsername, senderPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderUsername));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(targetEmail));
            message.setSubject(topic);
            message.setText(text);

            Transport.send(message);

        }catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendUpdate(OrderEnt ord, String newDeliveryStatus, String newPaymentStatus){
        String targetEmail = ord.getCustomer().getUsername();
        String topic = "Статус вашего заказа №"+ ord.getId() +"был обновлен";
        String text = "Статус оплаты:"+newPaymentStatus+"\nСтатус доставки:"+newDeliveryStatus;
        sendMessage(topic,text,targetEmail);
    }

    public void sendItemRequest(){
        Date date = new Date();
        String topic = "Заявка от "+date.toString();
        String text;

        ItemService itemService = new ItemService();
        StringBuilder stringBuilder = new StringBuilder();
        LinkedList<RequestedItem> requests = new LinkedList<>();
        requests = itemService.getRequestedItems();

        for (int i =0; i < requests.size(); i++) {

            RequestedItem request = (RequestedItem) requests.get(i);
            stringBuilder.append(request.getItemType().getName()+ "  Количество: "+ request.getAmount()+"\n");
        }

    }

}
