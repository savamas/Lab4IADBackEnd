package BusinessLogic.Services;

import BusinessLogic.Models.OrderEnt;
import BusinessLogic.Models.OrderItem;
import BusinessLogic.Models.Person;
import lombok.Data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class OrderService {

    @PersistenceContext(unitName = "personUnit")
    EntityManager entityManager;

    public void addOrder(Person customer, Collection<OrderItem> items, String paymentType, String paymentStatus, String deliveryType){
        OrderEnt order = new OrderEnt();
        order.setCustomer(customer);
        order.setOrderItemCollection(items);
        order.setPaymentType(paymentType);
        order.setPaymentStatus(paymentStatus);
        order.setDeliveryType(deliveryType);
        order.setDeliveryStatus("Received");
        Date dat = new Date();
        order.setDateCreated(dat);
        entityManager.persist(order);
    }

    public void setDeliveryStatus(String status, OrderEnt order){
        entityManager.getTransaction().begin();
        OrderEnt ord = entityManager.find(order.getClass(), order.getId());
        ord.setDeliveryStatus(status);
        entityManager.getTransaction().commit();
    }

    public void setPaymentStatus(String status, OrderEnt order){
        entityManager.getTransaction().begin();
        OrderEnt ord = entityManager.find(order.getClass(), order.getId());
        ord.setPaymentStatus(status);
        entityManager.getTransaction().commit();
    }

    public Collection<OrderEnt> getCustomerOrders(Person customer){
       return entityManager.find(customer.getClass(), customer.getId()).getOrderCollection();
    }

    public Collection<OrderEnt> getOrdersBetweenDates(Date date1, Date date2){
        return (Collection<OrderEnt>) entityManager.createQuery("select  from Orders p where p.dateCreated <=" +date2 + "AND p.dateCreated >= " + date1).getResultList();
    }

    public OrderEnt findOrder(int id){
        return entityManager.find(OrderEnt.class, id);
    }








}
