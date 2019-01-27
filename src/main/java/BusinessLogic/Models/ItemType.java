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
@Entity
@NamedQueries({
        @NamedQuery(name = "Person.FindAll", query = "select p from Person p"),
        @NamedQuery(name = "Person.FindByUsername", query = "select p from Person p where p.username = :name")
})
public class ItemType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    @Column(unique = true)
    private String name;
    private String imageUrl;
    private double price;
    @OneToMany(mappedBy = "itemType")
    private Collection<Property> propertyCollection;
    @ManyToOne
    @JoinColumn
    private ItemCategory category;
}

