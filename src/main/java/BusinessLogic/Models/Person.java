package BusinessLogic.Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
@NoArgsConstructor
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class Person {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String sex;

    @OneToMany(mappedBy = "customer")
    private Collection<OrderEnt> orderCollection;

    public Person(String username, String password, String firstName, String lastName, String sex, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.phoneNum = phone;
    }
}