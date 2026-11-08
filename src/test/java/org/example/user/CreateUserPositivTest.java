package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserPositivTest {
    private CreateUser user;
    private final UserClient userClient = new UserClient();

    @Test
    @DisplayName("Проверка кода ответа для ручки /api/auth/register")
    @Description("Переданы все поля и переданы валидными данными для ручки создания пользователя /api/auth/register")
    void postCreateUserStatusCode() {
        user = UserGenerator.generateValidUser();
        Response response = userClient.create(user);
        System.out.println(response.asString());
        assertEquals(200, response.statusCode(), "Ожидаем 200");
    }
}
