package ru.inno.xclient.api;

import io.restassured.common.mapper.TypeRef;
import org.springframework.stereotype.Component;
import ru.inno.xclient.model.api.Company;
import ru.inno.xclient.util.PropertiesType;
import ru.inno.xclient.util.PropertyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Component
public class CompanyServiceImpl implements CompanyService {
    private final String TEST_COMPANY_DATA_PREFIX = "TS_";
    private String uri;
    private final String login = "";
    private final String password = "";
    private String token = "";
    private final Map<String, String> headers = new HashMap<>();
    private final AuthService authService = AuthService.getInstance();
    private final PropertyService propertyService = PropertyService.getInstance();


    public CompanyServiceImpl() {
        this.uri = propertyService.getProperty(PropertiesType.API, "baseURI");
    }

    @Override
    public void setURI(String uri) {
        this.uri = uri;
    }

    @Override
    public List<Company> getAll() {
        return step("Получить список всех компаний по API", () ->
                given()
                        .baseUri(uri + "/company")
                        .headers(headers)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response()
                        .then()
                        .extract()
                        .body().as(new TypeRef<List<Company>>() {
                        })
        );
    }

    @Override
    public List<Company> getAll(boolean isActive) {
        return step("Получить список всех компаний по API с признаком isActive='" + isActive + "'", () ->
                given()
                        .baseUri(uri + "/company")
                        .headers(headers)
                        .param("active", isActive)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response()
                        .then()
                        .extract()
                        .body().as(new TypeRef<List<Company>>() {
                        })
        );
    }

    @Override
    public Company getById(int id) {
        return step("Получить компанию по API по id='" + id + "'", () ->
                given()
                        .baseUri(uri + "/company" + "/" + id)
                        .headers(headers)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response()
                        .then()
                        .extract()
                        .body().as(new TypeRef<Company>() {
                        })
        );
    }

    @Override
    //Нужна авторизация с правами admin
    public int create(String name) {
        return step("Создать компанию по API с именем '{" + name + "}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .baseUri(uri + "/company")
                        .contentType("application/json")
                        .body("{\"name\": \"" + TEST_COMPANY_DATA_PREFIX + name
                                + "\",\"description\": \"test\"}")
                        .when()
                        .post()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .contentType("application/json; charset=utf-8")
                        .extract().path("id")
        );
    }

    @Override
    //Нужна авторизация с правами admin
    public int create(String name, String description) {
        return step("Создать компанию по API с именем '{" + name + "}' и описанием {'" + description + "'}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .baseUri(uri + "/company")
                        .contentType("application/json")
                        .body("{\"name\": \"" + TEST_COMPANY_DATA_PREFIX + name
                                + "\",\"description\": \"" + description + "\"}")
                        .when()
                        .post()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(201)
                        .contentType("application/json; charset=utf-8")
                        .extract().path("id")
        );
    }

    @Override
    //Нужна авторизация с правами admin
    public void deleteById(int id) {
        step("Удалить по API компанию с id='" + id + "'", () -> {
            given()
                    .log().ifValidationFails()
                    .headers(headers)
                    .header("accept", "application/json")
                    .baseUri(uri + "/company/delete/" + id)
                    .contentType("application/json")
                    .when()
                    .get()
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200);
        });
    }

    @Override
    //Нужна авторизация с правами admin
    public Company edit(int id, String newName) {
        return step("Изменить компанию по API с id = '{" + id + "}' на наименование '{" + newName + "}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .baseUri(uri + "/company" + "/" + id)
                        .contentType("application/json")
                        .body("{\"name\": \"" + TEST_COMPANY_DATA_PREFIX + newName
                                + "\",\"description\": \"test\"}")
                        .when()
                        .patch()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        //TODO: написать bug-report, что при успешном обновлении выдаёт SC200 вместо SC202 (Swagger)
                        .contentType("application/json; charset=utf-8")
                        .extract()
                        .body().as(new TypeRef<Company>() {
                        })
        );
    }

    @Override
    //Нужна авторизация с правами admin
    public Company edit(int id, String newName, String newDescription) {
        return step("Изменить компанию по API с id = '{" + id + "}' на наименование '{" + newName + "}' " +
                "и описание {'" + newDescription + "'}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .baseUri(uri + "/company" + "/" + id)
                        .contentType("application/json")
                        .body("{\"name\": \"" + TEST_COMPANY_DATA_PREFIX + newName
                                + "\",\"description\": \"" + newDescription + "\"}")
                        .when()
                        .patch()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        //TODO: написать bug-report, что при успешном обновлении выдаёт SC200 вместо SC202 (Swagger)
                        .contentType("application/json; charset=utf-8")
                        .extract()
                        .body().as(new TypeRef<Company>() {
                        })
        );
    }

    @Override
    //Нужна авторизация с правами admin
    public Company changeStatus(int id, boolean isActive) {
        return step("Изменить компанию по API с id = '{" + id + "}' на состояние isActive = '{" + isActive + "}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .baseUri(uri + "/company" + "/" + id)
                        .contentType("application/json")
                        .body("{\"isActive\":\"" + isActive + " \"}")
                        .when()
                        .patch()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        //TODO: написать bug-report, что при успешном обновлении выдаёт SC200 вместо SC201 (Swagger)
                        .contentType("application/json; charset=utf-8")
                        .extract()
                        .body().as(new TypeRef<Company>() {
                        })
        );
    }

    @Override
    public void logIn(String login, String password) {
        step("Логин для CompanyService", () -> {
            this.token = authService.logIn(login, password);
            if (!token.isEmpty()) {
                //Если залогинены, то добавляем токен в headers
                headers.put("x-client-token", token);
            }
        });
    }

    @Override
    public void logOut() {
        step("Логаут для CompanyService", () -> {
            authService.logOut(login);
            token = "";
            //Если разлогинены, то убираем токен из headers
            headers.remove("x-client-token");
        });
    }
}
