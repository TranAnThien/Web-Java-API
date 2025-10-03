package ltweb.tuan09_2.model;

import lombok.Data;

@Data
public class RegisterUserModel {
    private String fullName;
    private String email;
    private String password;
}
