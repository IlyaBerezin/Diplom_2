package org.example.user;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String API_REGISTER_USER = "/api/auth/register";
    private static final String API_DELETE_USER = "/api/auth/user";
    private static final String API_LOGIN_USER = "/api/auth/login";


    public UserClient() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru";
    }

    @Step("Создание пользователя")
    public Response create(CreateUser user) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post(API_REGISTER_USER);

    }
    @Step("Получение токена")
    public String getToken(CreateUser user) {
        return create(user).path("accessToken");
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String token) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .when()
                .delete(API_DELETE_USER);
    }

    @Step("Логин пользователя")
    public Response login(LoginUser user) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post(API_LOGIN_USER);

    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response changeDataUserWithAuth(String token, CreateUser createUser) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", token)
                .body(createUser)
                .when()
                .patch(API_DELETE_USER);
    }

    @Step("Изменение данных пользователя без авторизации")
    public Response changeDataUserWithoutAuth(CreateUser createUser) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(createUser)
                .when()
                .patch(API_DELETE_USER);
    }

}
