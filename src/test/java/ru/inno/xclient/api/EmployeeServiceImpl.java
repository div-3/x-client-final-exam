package ru.inno.xclient.api;

import io.restassured.common.mapper.TypeRef;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import ru.inno.xclient.model.api.Employee;
import ru.inno.xclient.util.PropertiesType;
import ru.inno.xclient.util.PropertyService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

@Component
public class EmployeeServiceImpl implements EmployeeService {
    private final String PREFIX = "TS_";
    private String uri;
    private String login = "";
    private String password = "";
    private String token = "";
    private Map<String, String> headers = new HashMap<>();
    private AuthService authService = AuthService.getInstance();
    private Faker faker = new Faker(new Locale("ru"));
    private PropertyService propertyService = PropertyService.getInstance();


    public EmployeeServiceImpl() {
        this.uri = propertyService.getProperty(PropertiesType.API, "baseURI");
    }

    @Override
    public void setURI(String uri) {
        this.uri = uri;
    }

    @Override
    public List<Employee> getAllByCompanyId(int companyId) {
        return step("Получить по API список всех работников компании id = '{" + companyId + "}'", () ->
                given()
                        .baseUri(uri + "/employee")
                        .log().ifValidationFails()
                        .param("company", companyId)
                        .headers(headers)
                        .header("accept", "application/json")
                        .when()
                        .get()
                        .then()
                        .log().ifValidationFails()
                        .extract()
                        .response()
                        .then()
                        .extract()
                        .body().as(new TypeRef<List<Employee>>() {
                        })
        );
    }

    @Override
    public Employee getById(int id) {
        return step("Получить по API работникa id = '{" + id + "}'", () ->
                given()
                        .baseUri(uri + "/employee" + "/" + id)
                        .log().ifValidationFails()
                        .headers(headers)
                        .header("accept", "application/json")
                        .when()
                        .get()
                        .then()
                        .log().ifValidationFails()
                        .extract()
                        .response()
                        .then()
                        .extract()
                        .body().as(new TypeRef<Employee>() {
                        })
        );
    }

    @Override
    public Employee generateEmployee() {
        Employee employee = new Employee();
        employee.setId(0);
        String[] name = faker.name().nameWithMiddle().split(" ");
        employee.setFirstName(PREFIX + name[0]);
        employee.setLastName(name[2]);
        employee.setMiddleName(name[1]);
        employee.setCompanyId(0);
        employee.setEmail(faker.internet().emailAddress("a" + faker.number().digits(5)));
        employee.setUrl(faker.internet().url());

        //TODO: Написать BUG-репорт - при создании с неправильным телефоном возвращается ошибка 500 вместо 400
        employee.setPhone(faker.number().digits(10));
        employee.setBirthdate(faker.date().birthday("YYYY-MM-dd"));
        employee.setActive(true);
        return employee;
    }

    @Override
    public int create(Employee employee) {
        return step("Создать по API работникa и получить его id", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .baseUri(uri + "/employee")
                        .header("accept", "application/json")
                        .contentType("application/json; charset=utf-8")
                        .body(employee)
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
    public int update(Employee employee) {
        return step("Обновить по API информацию о работнике id = '{" + employee.getId() + "}'", () ->
                given()
                        .log().ifValidationFails()
                        .headers(headers)
                        .baseUri(uri + "/employee" + "/" + employee.getId())
                        .contentType("application/json")
                        .header("accept", "application/json")
                        .body("{\"lastName\": \"" + employee.getLastName() + "\"," +
                                "\"email\": \"" + employee.getEmail() + "\"," +
                                "\"url\": \"" + employee.getUrl() + "\"," +
                                "\"phone\": \"" + employee.getPhone() + "\"," +
                                "\"isActive\": " + employee.isActive() + "}")
                        .when()
                        .patch()
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .contentType("application/json; charset=utf-8")
                        .extract().path("id")
        );
    }

    @Override
    public void logIn(String login, String password) {
        step("Логин по API от EmployeeService");
        this.token = authService.logIn(login, password);
        if (!token.equals("")) {
            //Если залогинены, то добавляем токен в headers
            headers.put("x-client-token", token);
            this.login = login;
        }
    }

    @Override
    public void logOut() {
        authService.logOut(login);
        token = "";
        //Если разлогинены, то убираем токен из headers
        headers.remove("x-client-token");
        login = "";
    }

    @Override
    public PropertyService getPS() {
        return this.propertyService;
    }

}
