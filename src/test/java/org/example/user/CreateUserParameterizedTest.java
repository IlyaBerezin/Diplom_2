package org.example.user;

import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserParameterizedTest {

    private final UserClient userClient = new UserClient();

    // Источник данных
    static Stream<Arguments> invalidUserScenarios() {
        return Stream.of(
            Arguments.of(UserGenerator.generateEmailOnlyUser()),
                Arguments.of(UserGenerator.generateNameOnlyUser()),
                Arguments.of(UserGenerator.generatePasswordOnlyUser())
        );
    }

    @ParameterizedTest(name = "Регистрация без поля")
    @MethodSource("invalidUserScenarios")
    void createOrderWithColorVariants(CreateUser user) {
        Response response = userClient.create(user);

        assertEquals(403, response.statusCode());

        // Проверяем структуру ответа (success: false)
        boolean successFlag = response.path("success");
        assertEquals(false, successFlag, "Флаг success должен быть false при ошибке валидации");

        // Проверяем сообщение об ошибке
        String message = response.path("message");
        assertTrue(message != null && message.contains("Email, password and name are required fields"),
                "Сообщение должно содержать упоминание отсутствующего поля. Получено: " + message);
    }
}
