package ru.inno.xclient.api;

import io.qameta.allure.Step;
import io.restassured.common.mapper.TypeRef;
import org.springframework.stereotype.Component;
import ru.inno.xclient.model.api.Company;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Component
public class CompanyServiceImpl implements CompanyService {
    private String uri;
    private final static String PROPERTIES_FILE_PATH = "src/main/resources/API_x_client.properties";
    private String login = "";
    private String password = "";
    private String token = "";
    private boolean isAuth;
    private Map<String, String> headers = new HashMap<>();
    private AuthService authService = AuthService.getInstance();


    public CompanyServiceImpl() {
        this.uri = getProperties(PROPERTIES_FILE_PATH).getProperty("baseURI");
    }

    @Override
    public void setURI(String uri) {
        this.uri = uri;
    }

    @Override
    public List<Company> getAll() {
        return given()
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
                });
    }

    @Step("Получить список всех компаний по API с признаком isActive='{isActive}'")
    @Override
    public List<Company> getAll(boolean isActive) {
        step("Получить список всех компаний по API с признаком isActive='{isActive}'");
        return given()
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
                });
    }

    @Override
    public Company getById(int id) {
        return null;
    }

    @Override
    public int create(String name) {

        return 0;
    }

    @Override
    public int create(String name, String description) {
        return given()
                .log().ifValidationFails()
                .headers(headers)
                .header("accept", "application/json")
                .baseUri(uri + "/company")
                .contentType("application/json")
                .body("{\"name\": \"" + name + "\",\"description\": \"" + description + "\"}")
                .when()
                .post()
                .then()
                .log().ifValidationFails()
                .statusCode(201)
                .contentType("application/json; charset=utf-8")
                .extract().path("id");
    }

    @Override
    public void deleteById(int id) {
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
    }

    @Override
    public Company edit(int id, String newName) {
        return null;
    }

    @Override
    public Company edit(int id, String newName, String newDescription) {
        return null;
    }

    @Override
    public Company changeStatus(int id, boolean isActive) {
        return null;
    }

    @Override
    public void logIn(String login, String password) {
        this.token = authService.logIn(login, password);
        if (!token.equals("")) {
            //Если залогинены, то добавляем токен в headers
            headers.put("x-client-token", token);
        }
    }

    @Override
    public void logOut() {
        authService.logOut(login);
        token = "";
        //Если разлогинены, то убираем токен из headers
        headers.remove("x-client-token");
    }


    //Получить параметры из файла
    private Properties getProperties(String path) {
        File propFile = new File(path);
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(propFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
