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
public class OrderEnt {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column
    private String paymentType;
    private String status;
    private String paymentStatus;
    private String deliveryType;
    private String deliveryStatus;
    private Date dateCreated;
    private Date dateReceived;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Collection<OrderItem> orderItemCollection;

    @OneToOne
    @JoinColumn
    private Joint joint;

    @ManyToOne
    @JoinColumn
    private Person customer;

    @OneToOne
    @JoinColumn
    private Employee courier;
}