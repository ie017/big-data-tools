import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class Person {
    private String id;
    private String name;
    private String address;
    private Gender gender;
    private Date dob;
}
