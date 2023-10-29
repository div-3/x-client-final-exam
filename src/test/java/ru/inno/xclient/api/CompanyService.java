package ru.inno.xclient.api;


import io.qameta.allure.Step;
import org.springframework.stereotype.Component;
import ru.inno.xclient.model.api.Company;

import java.util.List;
@Component
public interface CompanyService {
    void setURI(String uri);
    @Step("Получить список всех компаний по API")
    List<Company> getAll();

    @Step("Получить список всех компаний по API с признаком isActive='{isActive}'")
    List<Company> getAll(boolean isActive);

    @Step("Получить компанию по API с ID='{id}'")
    Company getById(int id);

    @Step("Создать по API компанию с именем '{name}'")
    int create(String name);

    @Step("Создать по API компанию с именем '{name}' и описанием {'description'}")
    int create(String name, String description);

    @Step("Удалить по API компанию с id='{id}'")
    void deleteById(int id);

    @Step("")
    Company edit(int id, String newName);

    @Step("")
    Company edit(int id, String newName, String newDescription);

    @Step("")
    Company changeStatus(int id, boolean isActive);

    @Step("Логин по API от CompanyService")
    void logIn(String login, String password);

    @Step("Логаут по API от CompanyService")
    void logOut();
}
