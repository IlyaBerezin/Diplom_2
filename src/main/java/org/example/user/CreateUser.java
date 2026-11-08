package org.example.user;

public class CreateUser {
    private String email;
    private String password;
    private String name;

    public CreateUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public String getLogin() {
        return email;
    }

    public void setLogin(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static CreateUser from(CreateUser user) {
        return new CreateUser(user.getLogin(), user.getPassword(), user.getName());
    }
}
