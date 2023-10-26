package ru.inno.xclient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.db.CompanyRepoService;
import ru.inno.xclient.db.EmployeeRepoService;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class XClientApplicationTests {

    @Autowired
    private CompanyRepoService companyRepoService;

    @Autowired
    private EmployeeRepoService employeeRepoService;

    @AfterEach
    public void clearData() throws SQLException {
        employeeRepoService.clean("");
        companyRepoService.clean("");
    }

    @Test
    void contextLoads() throws SQLException {
        CompanyEntity ce;
        int id = companyRepoService.create("");
        ce = companyRepoService.getById(id);
        System.out.println("Компания: " + ce);
        System.out.println(ce.getId() + " name " + ce.getName());
    }

    @Test
    void contextLoads2() throws SQLException {

        System.out.println("\n--------------------------------------\n");

        int id = companyRepoService.create("");
        CompanyEntity company = companyRepoService.getById(id);

        System.out.println("\n--------------------------------------\n");
        System.out.println(company);
        System.out.println("\n--------------------------------------\n");

        List<EmployeeEntity> employees = new ArrayList<>();
        EmployeeEntity employee = employeeRepoService.create(id);
        employee = employeeRepoService.create(id);
        employee = employeeRepoService.create(id);
        employee = employeeRepoService.create(id);

        System.out.println("\n--------------------------------------\n");
        System.out.println(employee);
        System.out.println("\n--------------------------------------\n");

        employees = companyRepoService.loadEmployeeListToCompany(company).getEmployees();

        System.out.println("\n--------------------------------------\n");
        System.out.println("Из компании: " + employees);
        System.out.println("\n--------------------------------------\n");

        System.out.println("\n--------------------------------------\n");
        System.out.println("Из БД: " + employeeRepoService.getById(employee.getId()) + " id " + employee.getId());
        System.out.println("\n--------------------------------------\n");
        System.out.println("Из БД: " + employeeRepoService.getAllByCompanyId(company.getId()) + " id " + employee.getId());
        System.out.println("\n--------------------------------------\n");
    }

}
