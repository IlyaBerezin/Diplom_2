package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeDataUserAuthTest {
    private CreateUser createUser;
    private final UserClient userClient = new UserClient();
    private String token;

    @BeforeEach
    void setUp() {
        createUser = UserGenerator.generateValidUser();
        Response createResponse = userClient.create(createUser);
        token = createResponse.path("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            try { userClient.deleteUser(token); } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя для ручки /api/auth/user")
    @Description("Поле имени корректно изменено")
    void changeUserNameWithAuth() {
       String beforeName = createUser.getName();
       createUser.setName("NewName");
       Response newUserResponse = userClient.changeDataUserWithAuth(token, CreateUser.from(createUser));
       assertEquals(200, newUserResponse.statusCode());
       System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(true, successFlag, "Флаг success должен быть true при успешном входе");

        String accessToken = newUserResponse.path("accessToken");
       // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

    @Test
    @DisplayName("Изменение логина авторизованного пользователя для ручки /api/auth/user")
    @Description("Поле логина корректно изменено")
    void changeUserLoginWithAuth() {
        String beforeLogin = createUser.getLogin();
        createUser.setLogin("newLogin@teste.com");
        Response newUserResponse = userClient.changeDataUserWithAuth(token, CreateUser.from(createUser));
        assertEquals(200, newUserResponse.statusCode());
        System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(true, successFlag, "Флаг success должен быть true при успешном входе");

        String accessToken = newUserResponse.path("accessToken");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя для ручки /api/auth/user")
    @Description("Поле пароля корректно изменено")
    void changeUserPasswordWithAuth() {
        String beforePassword = createUser.getPassword();
        createUser.setPassword("newlogin12345");
        Response newUserResponse = userClient.changeDataUserWithAuth(token, CreateUser.from(createUser));
        assertEquals(200, newUserResponse.statusCode());
        System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(true, successFlag, "Флаг success должен быть true при успешном входе");

        String accessToken = newUserResponse.path("accessToken");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

}
