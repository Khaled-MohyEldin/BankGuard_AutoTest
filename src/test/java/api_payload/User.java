package api_payload;

import lombok.Data;

@Data
public class User {
    int id;
    String  username;
    String  email;
    String  password;
    String  fullName;
    String  phoneNumber;
    String  createdAt;
    String  updatedAt;
}

