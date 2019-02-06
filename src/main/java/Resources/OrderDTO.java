package Resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

    private int Id;
    private String paymentType;
    private String status = "Не указан";
    private String paymentStatus;
    private String deliveryType;
    private String deliveryStatus;
    private String dateCreated;
    private String dateReceived;
    private String phoneNum;


}
