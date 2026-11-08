package org.example.order;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String CREATE_ORDER_PATH = "/api/orders";
    private static final String GET_INGREDIENTS_PATH = "/api/ingredients";

    public OrderClient() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru";
    }

    @Step("Создание заказа авторизованным пользователем с ингредиентами")
    public Response createOrderWithAuth(String token, List<String> ingredients) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .body(Map.of("ingredients", ingredients))
                .when()
                .post(CREATE_ORDER_PATH);

    }

    @Step("Создание заказа неавторизованным пользователем с ингредиентами")
    public Response createOrderWithoutAuth(List<String> ingredients) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(Map.of("ingredients", ingredients))
                .when()
                .post(CREATE_ORDER_PATH);

    }

    @Step("Получить ингредиенты")
    public Response getIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(GET_INGREDIENTS_PATH);
    }

    @Step("Создание заказа без ингредиентов авторизованным пользователем")
    public Response createOrderWithoutIngredients(String token) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .when()
                .post(CREATE_ORDER_PATH);

    }

    @Step("Получить заказы авторизоавнным пользователем")
    public Response getOrdersWithAuth(String token) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .when()
                .get(CREATE_ORDER_PATH);
    }

    @Step("Получить заказы неавторизоавнным пользователем")
    public Response getOrdersWithoutAuth() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get(CREATE_ORDER_PATH);
    }

}
