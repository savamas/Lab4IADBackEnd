package BusinessLogic;

import BusinessLogic.Models.Person;
import interfaces.Encodable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountManager {


    @PersistenceContext(unitName = "personUnit")
    private EntityManager entityManager;

    @Inject
    Encodable encoder;

    public boolean checkPassword(String username, String password) {

        if (username.equals("") || password.equals("")) {
            return false;
        }

        Person person = findByUsername(username);
        if (person == null) {
            return false;
        }

        if (!encoder.authenticate(password.toCharArray() ,person.getPassword())) {
            return false;
        }
        return true;
    }

    public boolean checkOnDublicate(Person person) {
        return findByUsername(person.getUsername()) == null;
    }

    public Person findByUsername(String name) {
        try {
            return this.entityManager.createNamedQuery("Person.FindByUsername", Person.class)
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public void addUser(Person person) {
        String encodedPassword = encoder.hash(person.getPassword().toCharArray());
        person.setPassword(encodedPassword);
        this.entityManager.persist(person);
    }
}