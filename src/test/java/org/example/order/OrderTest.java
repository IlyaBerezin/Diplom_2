package org.example.order;

import io.restassured.response.Response;
import org.example.user.UserGenerator;
import org.example.user.UserClient;
import org.example.user.CreateUser;
import org.example.order.OrderClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private String token;
    private final OrderClient orderClient = new OrderClient(); // твой клиент для заказов
    private final UserClient userClient = new UserClient();

    @BeforeEach
    void setUp() {
        CreateUser createUser = UserGenerator.generateValidUser();
        Response createResponse = userClient.create(createUser);
        token = createResponse.path("accessToken");
        assertNotNull(token, "Токен не должен быть null после регистрации");
        System.out.println(createResponse.asString());
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            try {
                userClient.deleteUser(token);
                System.out.println("Пользователь успешно удалён.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Пользователь с валидным токеном успешно создаёт заказ с набором ингредиентов")
    void createOrderWithAuthSuccess() {
        Response ingredientsResponse = orderClient.getIngredients();
        System.out.println("Полный ответ API ингредиентов:");
        System.out.println(ingredientsResponse.asString());
        assertEquals(200, ingredientsResponse.statusCode(), "API ингредиентов должен возвращать 200");

        List<String> ingredientsId = ingredientsResponse.jsonPath().getList("data._id");
        assertTrue(!ingredientsId.isEmpty(), "Не удалось получить ингредиенты");

        // Защита от null-значений (на случай, если API вернёт битые данные)
        ingredientsId.removeIf(id -> id == null);
        assertTrue(!ingredientsId.isEmpty(), "После очистки список ингредиентов пуст");

        List<String> ingredients = ingredientsId.subList(0, Math.min(2, ingredientsId.size()));
        System.out.println("Используемые ингредиенты: " + ingredients);

        Response response = orderClient.createOrderWithAuth(token, ingredients);
        System.out.println(response.asString());
        assertEquals(200, response.statusCode(),
                "Создание заказа должно возвращать 200 OK, получен: " + response.statusCode());
        System.out.println(response.asString());
        assertNotNull(response.path("order"), "В ответе должен быть ID заказа в поле 'order'");
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    @Description("Пользователь без токена успешно создаёт заказ с набором ингредиентов")
    void createOrderWithoutAuthSuccess() {
        Response ingredientsResponse = orderClient.getIngredients();
        System.out.println("Полный ответ API ингредиентов:");
        System.out.println(ingredientsResponse.asString());
        assertEquals(200, ingredientsResponse.statusCode(), "API ингредиентов должен возвращать 200");

        List<String> ingredientsId = ingredientsResponse.jsonPath().getList("data._id");
        assertTrue(!ingredientsId.isEmpty(), "Не удалось получить ингредиенты");

        // Защита от null-значений (на случай, если API вернёт битые данные)
        ingredientsId.removeIf(id -> id == null);
        assertTrue(!ingredientsId.isEmpty(), "После очистки список ингредиентов пуст");

        List<String> ingredients = ingredientsId.subList(0, Math.min(2, ingredientsId.size()));
        System.out.println("Используемые ингредиенты: " + ingredients);

        Response response = orderClient.createOrderWithoutAuth(ingredients);
        System.out.println(response.asString());
        assertEquals(200, response.statusCode(),
                "Создание заказа должно возвращать 200 OK, получен: " + response.statusCode());
        System.out.println(response.asString());
        assertNotNull(response.path("order"), "В ответе должен быть ID заказа в поле 'order'");
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Должен возвращать ошибку 400")
    public void createOrderWithoutIngredients() {
    Response createOrderResponse = orderClient.createOrderWithoutIngredients(token);
    assertEquals(400, createOrderResponse.statusCode());
    assertEquals("Ingredient ids must be provided", createOrderResponse.path("message"));
    }

    @Test
    @DisplayName("Создание заказа c невалидным ингредиентом")
    @Description("Должен возвращать ошибку 500")
    public void createOrderIncorrectIngredient() {
        List<String> ingredients = new ArrayList<>(Arrays.asList("Invalid_id_of_ingredient", "60d3b41abdacab0026a733c6"));
        Response createOrderResponse = orderClient.createOrderWithAuth(token, ingredients);
        assertEquals(500, createOrderResponse.statusCode());
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    @Description("Должен возвращать 200")
    public void getOrderWithAuth() {
        Response ingredientsResponse = orderClient.getIngredients();
        List<String> ingredientsId = ingredientsResponse.jsonPath().getList("data._id");
        List<String> ingredients = ingredientsId.subList(0, Math.min(2, ingredientsId.size()));
        Response response = orderClient.createOrderWithAuth(token, ingredients);
        Response getOrderResponse = orderClient.getOrdersWithAuth(token);
        assertEquals(200, getOrderResponse.statusCode());
        System.out.println(getOrderResponse.asString());
    }
    @Test
    @DisplayName("Получение заказа неавторизованным пользователем")
    @Description("Должен возвращать 401")
    public void getOrderWithoutAuth() {
        Response ingredientsResponse = orderClient.getIngredients();
        List<String> ingredientsId = ingredientsResponse.jsonPath().getList("data._id");
        List<String> ingredients = ingredientsId.subList(0, Math.min(2, ingredientsId.size()));
        Response response = orderClient.createOrderWithAuth(token, ingredients);
        Response getOrderResponse = orderClient.getOrdersWithoutAuth();
        assertEquals(401, getOrderResponse.statusCode());
        System.out.println(getOrderResponse.asString());
    }

}
