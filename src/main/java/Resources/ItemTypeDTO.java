package Resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemTypeDTO {

    int id;
    String name;
    String url;
    Double price;
    String description;

}
