package Resources;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String sex;

}
