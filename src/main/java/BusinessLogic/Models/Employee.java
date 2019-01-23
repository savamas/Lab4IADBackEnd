package BusinessLogic.Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(unique = true)
    private String occupation;

    private String status;
    @ManyToOne
    @JoinColumn
    private Joint joint;
    @OneToOne
    @JoinColumn
    private Person person;

    public Employee(String occupation, Joint joint, String status) {
        this.status = status;
        this.occupation = occupation;
        this.joint = joint;

}
}