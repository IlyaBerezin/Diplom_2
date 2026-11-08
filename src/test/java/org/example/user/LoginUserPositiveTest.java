package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.example.user.UserClient;
import org.example.user.CreateUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUserPositiveTest {
    private CreateUser createUser;
    private LoginUser loginUser;
    private final UserClient userClient = new UserClient();
    private String token;

    @BeforeEach
    void setUp() {
        createUser = UserGenerator.generateValidUser();
        loginUser = new LoginUser(createUser.getLogin(), createUser.getPassword());
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            try { userClient.deleteUser(token); } catch (Exception e) {}
        }
    }

    @Test
    @DisplayName("Проверка кода ответа для ручки /api/auth/login")
    @Description("Переданы все поля и переданы валидными данными для ручки входа пользователя /api/auth/login")
    void postLoginUserStatusCode() {
        Response createResponse = userClient.create(createUser);
        System.out.println(createResponse.asString());
        assertEquals(200, createResponse.statusCode(), "Пользователь должен быть создан");
        Response loginResponse = userClient.login(loginUser);
        System.out.println(loginResponse.asString());
        assertEquals(200, loginResponse.statusCode(), "Ожидаем 200");
        boolean successFlag = loginResponse.path("success");
        assertEquals(true, successFlag, "Флаг success должен быть true при успешном входе");

        String accessToken = loginResponse.path("accessToken");
        assertTrue(!accessToken.isEmpty(), "accessToken не должен быть пустой строкой");
        // Сохраняем токен для удаления в tearDown
        this.token = accessToken;
    }
}
