package BusinessLogic.Services;

import BusinessLogic.Models.*;
import com.google.gson.JsonObject;
import lombok.Data;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import com.google.gson.*;



@Stateless
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

    public  List<ItemCategory> getDistinctCategories(){
        return (List<ItemCategory>)entityManager.createQuery("select DISTINCT c from ItemCategory c ").getResultList();
    }

    public List<FilterSetting> getFiltersByCategoryId(int categoryId){
        List<Property> properties = entityManager.createQuery("select DISTINCT p from Property p where p.itemType.category.Id ="+categoryId).getResultList();
        List<FilterSetting> filters = new LinkedList<>();
        System.out.println("TATATATA");



        List<Property> propertiesClone = new LinkedList<>();
        propertiesClone = properties;
        Iterator<Property> itr = properties.listIterator();
        while(itr.hasNext()) {
            FilterSetting filter = new FilterSetting();
            filter.setFilterValues(new LinkedList<>());

            boolean first = true;
            while(itr.hasNext()) {
                Property prop = itr.next();
                if(prop == null)
                    break;
                if (first) {

                    first = false;
                    filter.setFilterName(prop.getName());
                }

                if(prop.getName().equals(filter.getFilterName())) {
                    filter.addValue(prop.getValue());
                    itr.remove();
                }

            }
            filters.add(filter);
            itr = properties.listIterator();
        }
        return  filters;
    }



    public List<ItemType> getItemsByCategory(int categoryId) {

        return (List<ItemType>)entityManager.createQuery("select e from ItemType e where e.category.Id =" + categoryId).getResultList();
    }


    public List<ItemType> getFilteredItems(int categoryId, LinkedList<FilterSetting> filters){

        List<ItemType> types = entityManager.createQuery("select e from ItemType e where e.category.Id ="+categoryId).getResultList();
        LinkedList<ItemType> result = new LinkedList<>();


        for (ItemType type : types) {

            boolean nextType = false;


            LinkedList<Property> properties = (LinkedList<Property>)type.getPropertyCollection();


            if(!nextType) {


                for (FilterSetting setting : filters) {


                    for (Property property : properties) {

                        boolean success = false;

                        if (property.getName().equals(setting.getFilterName())) {
                            for (String filterValue : setting.getFilterValues()) {


                                if (property.getValue().equals(filterValue)) {
                                    success = true;
                                    break;
                                }

                            }
                            if (!success) {
                                nextType = true;
                                break;
                            }

                        }
                    }
                    if(nextType)
                        break;
                }
            }
            if(!nextType)
                result.add(type);
        }

        return  result;
    }

    public String itemTypesToJSON(List<ItemType> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public void addCategory(String name){
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setName(name);

        entityManager.persist(itemCategory);

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

