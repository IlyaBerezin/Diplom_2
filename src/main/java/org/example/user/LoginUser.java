package org.example.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUser {
    private String email;
    private String password;
    //private String name;

    public static LoginUser credsFromUser(LoginUser user) {
        return new LoginUser(user.getEmail(), user.getPassword());
    }
}
