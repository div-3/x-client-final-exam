package ru.inno.xclient.api;

import io.restassured.filter.log.LogDetail;
import ru.inno.xclient.util.PropertiesType;
import ru.inno.xclient.util.PropertyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

//--------------------------
//Синглтон-класс авторизации
//--------------------------
public class AuthService {
    private final Map<String, List<String>> authInfo = new HashMap<>();   //HashMap<login, <password, token>>
    private String basePathString = "";
    private final PropertyService propertyService = PropertyService.getInstance();


    private AuthService() {
        basePathString = propertyService.getProperty(PropertiesType.API, "baseURI");
    }

    public static AuthService getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    public String logIn(String login, String password) {
        if (login.isEmpty() || password.isEmpty()) {
            login = propertyService.getProperty(PropertiesType.API, "login");
            password = propertyService.getProperty(PropertiesType.API, "password");
        }
        if (authInfo.containsKey(login) && authInfo.get(login).get(0).equals(password))
            return authInfo.get(login).get(1);
        String token =
                given()
                        .baseUri(basePathString + "/auth/login")
                        .log().ifValidationFails(LogDetail.ALL)   //Логирование при ошибке
                        .contentType("application/json; charset=utf-8")
                        .body("{\"username\": \"" + login + "\", \"password\": \"" + password + "\"}")
                        .when()
                        .post()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(201)             //Проверка статус-кода
                        .contentType("application/json; charset=utf-8")     //Проверка content-type
                        .extract().path("userToken").toString();

        if (!token.isEmpty()) {
            if (authInfo.containsKey(login)) {
                authInfo.replace(login, List.of(password, token));   //Если пользователь уже получал токен, то заменяем
            } else {
                authInfo.put(login, List.of(password, token));
            }
        }
        return token;
    }

    public void logOut(String login) {
        authInfo.entrySet().removeIf(entry -> entry.getKey().contains(login));
    }

    public static class SingletonHolder {
        public static final AuthService HOLDER_INSTANCE = new AuthService();
    }
}