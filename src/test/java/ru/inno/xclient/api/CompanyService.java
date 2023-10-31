package ru.inno.xclient.api;


import org.springframework.stereotype.Component;
import ru.inno.xclient.model.api.Company;
import ru.inno.xclient.util.PropertyService;

import java.util.List;

@Component
public interface CompanyService {
    void setURI(String uri);

    List<Company> getAll();

    List<Company> getAll(boolean isActive);

    Company getById(int id);

    int create(String name);

    int create(String name, String description);

    void deleteById(int id);

    Company edit(int id, String newName);

    Company edit(int id, String newName, String newDescription);

    Company changeStatus(int id, boolean isActive);

    void logIn(String login, String password);

    void logOut();

    PropertyService getPS();
}
