package org.example.user;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUserNegativeTest {
    private CreateUser createUser;
    private final UserClient userClient = new UserClient();
    private String token;

    @BeforeEach
    void setUp() {
        createUser = UserGenerator.generateValidUser();
        Response createResponse = userClient.create(createUser);
        // Проверка, что регистрация прошла успешно
        assertEquals(200, createResponse.statusCode());
        this.token = createResponse.path("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            try { userClient.deleteUser(token); } catch (Exception e) {}
        }
    }
    // Источник данных
    static Stream<Arguments> invalidUserScenarios() {
        return Stream.of(
                Arguments.of(UserGenerator.generateEmailOnlyUser()),
                Arguments.of(UserGenerator.generatePasswordOnlyUser())
        );
    }

    @ParameterizedTest(name = "Вход пользователя с некорректным обязательным полем")
    @MethodSource("invalidUserScenarios")
    void postLoginUserIncorrect(CreateUser user) {
        LoginUser invalidUser = new LoginUser(user.getLogin(), user.getPassword());
        Response loginUserResponse = userClient.login(invalidUser);
        assertEquals(401, loginUserResponse.statusCode(), "Ожидаем 401");

        boolean successFlag = loginUserResponse.path("success");
        assertEquals(false, successFlag, "Флаг success должен быть false");
        // Проверяем сообщение об ошибке
        String message = loginUserResponse.path("message");
        assertTrue(message != null && message.contains("email or password are incorrect"),
                "Сообщение должно содержать упоминание отсутствующего поля. Получено: " + message);
    }
}
