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
})
public class Hit {
    @Id @GeneratedValue
    private int id;
    private double x;
    private double y;
    private double r;
    private String isInArea;

    public Hit(double x, double y, double r, String isInArea) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInArea = isInArea;
    }
}