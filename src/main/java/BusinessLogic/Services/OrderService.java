package BusinessLogic.Services;

import BusinessLogic.Models.Employee;
import BusinessLogic.Models.OrderEnt;
import BusinessLogic.Models.OrderItem;
import BusinessLogic.Models.Person;
import lombok.Data;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;

import static javax.ejb.TransactionAttributeType.REQUIRED;

@Data
@Stateless
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
        order.setDeliveryStatus("В обработке");
        order.setStatus("В обработке");
        Date dat = new Date();
        order.setDateCreated(dat);
        entityManager.persist(order);
        for (OrderItem item : order.getOrderItemCollection()) {
            item.setOrder(order);
        }
        entityManager.flush();
    }

    public void setDeliveryStatus(String status, OrderEnt order){

        OrderEnt ord = entityManager.find(order.getClass(), order.getId());
        ord.setDeliveryStatus(status);

    }

    public void setPaymentStatus(String status, OrderEnt order){

        OrderEnt ord = entityManager.find(order.getClass(), order.getId());
        ord.setPaymentStatus(status);

    }

    public Collection<OrderEnt> getCustomerOrders(Person customer){

       return (Collection<OrderEnt>) entityManager.createQuery("select e from OrderEnt e where e.customer.Id =  "+customer.getId()).getResultList();
    }

    public Collection<OrderEnt> getOrdersBetweenDates(Date date1, Date date2){
        return (Collection<OrderEnt>) entityManager.createQuery("select  from Orders p where p.dateCreated <=" +date2 + "AND p.dateCreated >= " + date1).getResultList();
    }

    public OrderEnt findOrder(int id){
        return entityManager.find(OrderEnt.class, id);
    }

    public Person findPerson(int id){
        return entityManager.find(Person.class, id);
    }

    public Collection<OrderItem> getOrderContents(int id){
        return (Collection<OrderItem>) entityManager.createQuery("select c from OrderItem c where c.order.Id ="+ id).getResultList();
    }

    public Collection<OrderEnt> getUnclaimedOrders(){

        return (Collection<OrderEnt>) entityManager.createQuery("select e from OrderEnt e where (e.courier IS NULL)  AND ((e.status <> 'Completed')OR (e.status is null ))").getResultList();

    }

    public void claimOrder(Person courier, int orderId){
        Employee courierEm = (Employee)entityManager.createQuery("select e from Employee e where e.person.Id = "+courier.getId()).getSingleResult();
        OrderEnt order = entityManager.find(OrderEnt.class, orderId);
        order.setCourier(courierEm);
    }

    public Collection<OrderEnt> getActiveOrders(int id){

        return (Collection<OrderEnt>) entityManager.createQuery("select e from OrderEnt e where ((e.status <> 'Completed')OR (e.status is null )) AND e.courier.person.Id = "+id).getResultList();

    }








}
