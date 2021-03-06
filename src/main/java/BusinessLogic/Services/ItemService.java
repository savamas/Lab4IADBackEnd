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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i : ids) {
            stringBuilder.append("'");
            stringBuilder.append(i);
            stringBuilder.append("'");
            stringBuilder.append(",");
        }
        stringBuilder.setLength(stringBuilder.length()-1);
        stringBuilder.append(")");

        String idsString = stringBuilder.toString();

        List<TypeInJoint> list = (List<TypeInJoint>) entityManager.createQuery("select c from TypeInJoint c where c.itemType.Id in "+ idsString).getResultList();
        Iterator<TypeInJoint> itr = list.listIterator();
        while(itr.hasNext()) {

            int id = 0;
            int amount = 0;

            boolean first = true;
            while(itr.hasNext()) {
                TypeInJoint typeInJoint = itr.next();
                if(typeInJoint == null)
                    break;
                if (first) {

                    first = false;
                    id = typeInJoint.getItemType().getId();
                }

                if(typeInJoint.getItemType().getId() == id) {
                    amount = amount + typeInJoint.getAmount();
                    itr.remove();
                }

            }
            map.put(id, amount);
            itr = list.listIterator();
        }

        return map;
    }


    public Collection<Property> getPropertiesByItemTypeId(int id)
    {
        return (Collection<Property>)entityManager.createQuery("select c from Property c where c.itemType.Id = "+ id).getResultList();
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

    public void addItemType(ItemType item){
        entityManager.getTransaction().begin();
        entityManager.persist(item);
        entityManager.getTransaction().commit();
    }

    public void addCategory(ItemCategory item){
        entityManager.getTransaction().begin();
        entityManager.persist(item);
        entityManager.getTransaction().commit();
    }

    public void addProperty(Property item){
        entityManager.getTransaction().begin();
        entityManager.persist(item);
        entityManager.getTransaction().commit();
    }

    public List<Date> getBookedDates(){
        return (List<Date>)entityManager.createQuery("select e.startDate from OrderItem e where e.itemType.category.Id = 2").getResultList();
    }


    public List<ItemCategory> getDistinctCategories(){
        return (List<ItemCategory>)entityManager.createQuery("select DISTINCT c from ItemCategory c ").getResultList();
    }

    public List<FilterSetting> getFiltersByCategoryId(int categoryId){
        List<Property> properties = entityManager.createQuery("select DISTINCT p from Property p where p.itemType.category.Id ="+categoryId).getResultList();
        List<FilterSetting> filters = new LinkedList<>();



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

                    boolean skip = false;
                    Iterator<String> iter = filter.getFilterValues().listIterator();
                    while (iter.hasNext()){
                        if(iter.next().equals(prop.getValue())){
                            itr.remove();
                            skip = true;
                        }


                    }
                    if(!skip){
                        filter.addValue(prop.getValue());
                        itr.remove();
                    }

                }

            }
            filters.add(filter);
            itr = properties.listIterator();
        }
        return  filters;
    }



    public List<ItemType> getItemsByCategory(int categoryId) {


        List<ItemType> list = entityManager.createQuery("select e from ItemType e where e.category.Id =" + categoryId).getResultList();

        return list;
    }


    public List<ItemType> getFilteredItems(int categoryId, List<FilterSetting> filters){

        List<ItemType> types = entityManager.createQuery("select e from ItemType e where e.category.Id ="+categoryId).getResultList();
        ArrayList<ItemType> result = new ArrayList<>();


        for (ItemType type : types) {

            boolean nextType = false;


            Collection<Property> properties = type.getPropertyCollection();


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


    public List<ItemType> getSearchedItems(String query, int categoryId){
        List<ItemType> list = entityManager.createQuery("select e from ItemType e where e.category.Id ="+categoryId+" AND e.name like '%" +query+ "%'" ).getResultList();
        return list;
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


    public String getPersonOccupation(int id){

         Employee employee = new Employee();
         try {
             employee = (Employee) entityManager.createQuery("select e from Employee e where e.person.Id = " + id).getSingleResult();
         }catch (Exception e){
             employee = null;
         }

         String occupation;
         if(employee == null)
             occupation = "No occupation";
         else {
             if((employee.getOccupation() == null)||(employee.getOccupation().length() == 0)||(employee.getOccupation().isEmpty()))
                 occupation = "Unknown occupation";
             else
                 occupation = employee.getOccupation();
         }

         return  occupation;
    }

    public List<Property> getItemProperties( int id){
        return (List<Property>)entityManager.createQuery("select e from Property e where e.itemType.Id ="+id).getResultList();
    }

    public String getItemDescription(ItemType item){
        List<Property> properties = getItemProperties(item.getId());
        StringBuilder stringBuilder = new StringBuilder();
        for (Property prop: properties) {
            stringBuilder.append(prop.getName());
            stringBuilder.append(": ");
            stringBuilder.append(prop.getValue());
            stringBuilder.append("\n");
        }


       /* if(item.getDescription() == null || item.getDescription().length() == 0)
            stringBuilder.append("\n\nНет описания");
        else {

            stringBuilder.append("\n\nОписание: \n");
            stringBuilder.append(item.getDescription());
        }*/

        return stringBuilder.toString();
    }

}

