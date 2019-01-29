package Resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDTO {
    int amount;
    String name;
    String url;
    Double price;
    String date;
    int categoryId;
}
