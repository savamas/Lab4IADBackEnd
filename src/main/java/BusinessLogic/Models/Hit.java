package BusinessLogic.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@NoArgsConstructor
@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = "Hit.FindAll", query = "select h from Hit h"),
        @NamedQuery(name = "Hit.FindByUsername", query = "select h from Hit h where h.person = :person_id")
})
public class Hit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double x;
    private double y;
    private double r;
    private String isInArea;
    @ManyToOne
    private Person person;

    public Hit(double x, double y, double r, String isInArea, Person person) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInArea = isInArea;
        this.person = person;
    }
}