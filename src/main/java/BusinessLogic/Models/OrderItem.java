package BusinessLogic.Models;

import lombok.Data;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column
    private int amount;
    private boolean rentStatus;
    private int rentDuration;

    @ManyToOne
    @JoinColumn
    private OrderEnt order;

    @ManyToOne
    @JoinColumn
    private ItemType itemType;

    private Date startDate;
}