package BusinessLogic.Models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Date;

@XmlRootElement
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class TypeInJoint {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column
    private int amount;

    @ManyToOne
    @JoinColumn
    private ItemType itemType;

    @ManyToOne
    @JoinColumn
    private Joint joint;



}