package org.example.user;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateUserNegativTest {

        private CreateUser user;
        private final UserClient userClient = new UserClient();
        private String token;

        @AfterEach
        void tearDown() {
            if (token != null && !token.isEmpty()) {
                try { userClient.deleteUser(token); } catch (Exception e) {}
            }
        }

        @Test
        @DisplayName("Проверка кода ответа 403 для ручки /api/auth/register")
        @Description("Проверка, что при попытке создать пользователя который уже есть в системе, возвращается 403 Conflict")
        void postCreateDuplicateUserStatusCode() {
            user = UserGenerator.generateValidUser();
            token = userClient.getToken(user);
            Response secondResponse = userClient.create(user);
            assertEquals(403, secondResponse.statusCode(), "Повторное создание код 409");
        }
}
