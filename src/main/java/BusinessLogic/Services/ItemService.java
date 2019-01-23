package BusinessLogic.Services;

import BusinessLogic.Models.*;
import lombok.Data;

import javax.json.stream.JsonParser;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Order;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Data
public class ItemService {

    @PersistenceContext(unitName = "personUnit")
    EntityManager entityManager;
    public Map<Integer,Integer> getItemsAmountByIds(Collection<Integer> ids){

        Map<Integer,Integer> map = new HashMap<>();
        List list = entityManager.createQuery("select c.itemType as id, sum(c.amount) as amount from TypeInJoint c group by c.amount ").getResultList();
        for(int i =0; i < list.size(); i++){

            Object[] obj = (Object[]) list.get(i);
            map.put((int)obj[0], (int)obj[1]);

        }

        return map;
    }

    public ItemType getItemTypeById(int id){
        return entityManager.find(ItemType.class, id);

    }

    public void addRequestedItem(OrderItem item, int amount){
        entityManager.getTransaction().begin();
        RequestedItem requestedItem = new RequestedItem();
        requestedItem.setAmount(amount);
        requestedItem.setItemType(item.getItemType());
        entityManager.persist(requestedItem);
        entityManager.getTransaction().commit();
    }

    public LinkedList<RequestedItem> getRequestedItems(){
        LinkedList<RequestedItem> requests = (LinkedList<RequestedItem>)entityManager.createQuery("select e from RequestedItem e");
        return  requests;
        }


     public void flushRequestedItems(){
        entityManager.createQuery("delete from RequestedItem");
    }

    public LinkedList<OrderEnt> getAllUnfinishedOrders(){
        return (LinkedList<OrderEnt>)entityManager.createQuery("select e from OrderEnt e  where e.status <> 'Fulfilled' ").getResultList();
    }

}
