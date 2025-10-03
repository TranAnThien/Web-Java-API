package ltweb.tuan09_2.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
}
