package BusinessLogic.Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement
@Data
@NoArgsConstructor
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class Joint {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String occupation;
    private String jointId;
    private String status;
    @OneToMany(mappedBy = "joint")
    Collection<Employee> employeeList;

    public Joint(String occupation, String jointId, String status) {
        this.status = status;
        this.occupation = occupation;
        this.jointId = jointId;

    }
}