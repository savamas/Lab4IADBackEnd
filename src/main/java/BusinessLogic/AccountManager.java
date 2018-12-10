package BusinessLogic;

import BusinessLogic.Models.Hit;
import BusinessLogic.Models.Person;
import interfaces.Checkable;
import interfaces.Encodable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AccountManager {

    @PersistenceContext(unitName = "personUnit")
    private EntityManager entityManager;

    @Inject
    Checkable checker;

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

    public boolean checkHit(double x, double y ,double r) {
        boolean isInArea = checker.check(x, y, r);
        if (isInArea) {
            this.entityManager.persist(new Hit(x, y, r, "Yes"));
        } else {
            this.entityManager.persist(new Hit(x, y, r, "No"));
        }
        return isInArea;
    }

    public List<Hit> findAllHits() {
        try {
            return this.entityManager.createNamedQuery("Hit.FindAll", Hit.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}