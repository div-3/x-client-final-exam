package ru.inno.xclient.api;


import ru.inno.xclient.model.api.Employee;
import ru.inno.xclient.util.PropertyService;

import java.util.List;

public interface EmployeeService {
    void setURI(String uri);

    List<Employee> getAllByCompanyId(int companyId);

    Employee generateEmployee();

    Employee getById(int id);

    int create(Employee employee);

    int update(Employee employee);

    void logIn(String login, String password);

    void logOut();

    PropertyService getPS();
}
