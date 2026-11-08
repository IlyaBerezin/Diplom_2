package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChangeDataUserUnauthTest {
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
            try { userClient.deleteUser(token); } catch (Exception e) {}
        }
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя для ручки /api/auth/user")
    @Description("Выдаёт ошибку 401")
    void changeUserNameWithAuth() {
        String beforeName = createUser.getName();
        createUser.setName("UnauthName");
        Response newUserResponse = userClient.changeDataUserWithoutAuth(CreateUser.from(createUser));
        assertEquals(401, newUserResponse.statusCode());
        System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(false, successFlag, "Флаг success должен быть false");
        String message = newUserResponse.path("message");
        assertTrue(message != null && message.contains("You should be authorised"));
        String accessToken = newUserResponse.path("accessToken");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

    @Test
    @DisplayName("Изменение логина неавторизованного пользователя для ручки /api/auth/user")
    @Description("Выдаёт ошибку 401")
    void changeUserLoginWithAuth() {
        String beforeLogin = createUser.getLogin();
        createUser.setLogin("unauthName@mail.ru");
        Response newUserResponse = userClient.changeDataUserWithoutAuth(CreateUser.from(createUser));
        assertEquals(401, newUserResponse.statusCode());
        System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(false, successFlag, "Флаг success должен быть false");
        String message = newUserResponse.path("message");
        assertTrue(message != null && message.contains("You should be authorised"));
        String accessToken = newUserResponse.path("accessToken");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя для ручки /api/auth/user")
    @Description("Выдаёт ошибку 401")
    void changeUserPasswordWithAuth() {
        String beforePassword = createUser.getPassword();
        createUser.setPassword("newlogin12345");
        Response newUserResponse = userClient.changeDataUserWithoutAuth(CreateUser.from(createUser));
        assertEquals(401, newUserResponse.statusCode());
        System.out.println(newUserResponse.asString());

        boolean successFlag = newUserResponse.path("success");
        assertEquals(false, successFlag, "Флаг success должен быть false");
        String message = newUserResponse.path("message");
        assertTrue(message != null && message.contains("You should be authorised"));
        String accessToken = newUserResponse.path("accessToken");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }

}
