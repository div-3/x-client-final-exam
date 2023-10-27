package ru.inno.xclient;

import net.datafaker.Faker;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.api.CompanyService;
import ru.inno.xclient.api.EmployeeService;
import ru.inno.xclient.db.CompanyRepoService;
import ru.inno.xclient.db.CompanyRepoServiceJDBCImpl;
import ru.inno.xclient.db.EmployeeRepoService;
import ru.inno.xclient.model.api.Company;
import ru.inno.xclient.model.api.Employee;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XClientApplicationTests {

    @Autowired
    private CompanyRepoService companyRepoService;
    @Autowired
    private EmployeeRepoService employeeRepoService;
    @Autowired
    private CompanyService companyApiService;
    @Autowired
    private EmployeeService employeeAPIService;
    private Faker faker = new Faker(new Locale("RU"));
    private final String TEST_EMPLOYEE_DATA_PREFIX = "TS_";

    @AfterEach
    public void clearData() throws SQLException {
//        employeeRepoService.clean("");
//        companyRepoService.clean("");
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

    @Test
    @DisplayName("1. Проверить, что список компаний фильтруется по параметру active")
    public void shouldApiFilterCompaniesByActive() throws SQLException {
        //1. Получаем список активных компаний по API
        List<Company> companiesAPI = companyApiService.getAll(true); //.stream().forEach(c -> System.out.println("id " + c.getId() + " name: " + c.getName()));

        //2. Получаем список активных компаний из BD
        List<CompanyEntity> companiesDB = companyRepoService.getAll(true, false);   //Активные и неудалённые компании

        //3. Проверяем длинны списков
        assertEquals(companiesDB.size(), companiesAPI.size());

        //4. Получаем списки companyId
        List<Integer> companiesIdApi = companiesAPI.stream().map(c -> c.getId()).toList();
        List<Integer> companiesIdDB = companiesDB.stream().map(c -> c.getId()).toList();
//        System.out.println("API: " + companiesIdApi + " " + companiesIdApi.size());
//        System.out.println("DB: " + companiesIdDB + " " + companiesIdDB.size());

        //5. Проверяем, что списки совпали
        assertTrue(companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi));
//        assertEquals(companiesIdDB, companiesIdApi);      //Корректно не проверяет, т.к. элементы в списках могут быть в разном порядке

        //6. Получаем список НЕактивных компаний по API
        companiesAPI = companyApiService.getAll(false); //.stream().forEach(c -> System.out.println("id " + c.getId() + " name: " + c.getName()));

        //7. Получаем список НЕактивных компаний из BD
        companiesDB = companyRepoService.getAll(false, false);   //Активные и неудалённые компании

        //8. Проверяем длинны списков
        assertEquals(companiesDB.size(), companiesAPI.size());

        //9. Получаем списки companyId
        companiesIdApi = companiesAPI.stream().map(c -> c.getId()).toList();
        companiesIdDB = companiesDB.stream().map(c -> c.getId()).toList();
//        System.out.println("API: " + companiesIdApi + " " + companiesIdApi.size());
//        System.out.println("DB: " + companiesIdDB + " " + companiesIdDB.size());

        //10. Проверяем, что списки совпали
        assertTrue(companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi));
    }

    @Test
    @DisplayName("2. Проверить создание сотрудника в несуществующей компании")
    public void shouldNotCreateEmployeeToAbsentCompany() throws SQLException {
        //1.Авторизоваться по API
        employeeAPIService.logIn("", "");

        //2. Создать объект Employee
        Employee employee = createEmployeeWithoutCompanyId();

        //3. Получить ID последней созданной компании из DB
        int lastCompanyId = companyRepoService.getLast().getId();

        //4. Установить для Employee номер несуществующей компании
        employee.setCompanyId(lastCompanyId + 100);
//        employee.setCompanyId(lastCompanyId);     //Проверка, что создаётся при правильном номере компании

        //5. Проверить, что при попытке создания Employee через API выбрасывается исключение
        assertThrows(AssertionError.class, ()->{
            employeeAPIService.create(employee);
        });

        //6. Проверить, что Employee с тестовыми данными нет в DB
        assertEquals(0, employeeRepoService.getAllByFirstNameLastNameMiddleName(
                employee.getFirstName(), employee.getLastName(), employee.getMiddleName()).size());

    }

    @Test
    @DisplayName("3. Проверить, что неактивный сотрудник не отображается в списке")
    public void shouldNotGetNonActiveEmployee() throws SQLException {
        //1. Создать Company в DB
        int companyId = companyRepoService.create("");

        //2. Создать Employee в DB
        EmployeeEntity employee = employeeRepoService.create(companyId);

        //3. У Employee установить isActive = false
        employee.setActive(false);
        employeeRepoService.save(employee);

        //4. Проверить, что неактивный сотрудник не отображается в списке при запросе по Company ID через API
        assertNull(employeeAPIService.getAllByCompanyId(companyId));

        //5. Проверить, что неактивный сотрудник не отображается при запросе по ID через API
        assertNull(employeeAPIService.getById(employee.getId()));
    }

    @Test
    @DisplayName("4. Проверить, что у удаленной компании проставляется в БД поле deletedAt")
    public void shouldFillDeletedAtToDeletedCompany() throws SQLException {

        int companyId = companyRepoService.create("");

//        CompanyEntity companyDB = companyRepoService.getById(companyId);
//        System.out.println("\n-----------------------------\n");
//        System.out.println(companyDB);
//        System.out.println("\n-----------------------------\n");
//
//        assertNull(companyDB.getDeletedAt());

        companyApiService.logIn("","");
        companyApiService.deleteById(companyId);

        CompanyEntity companyDB = companyRepoService.getById(companyId);
        System.out.println("\n-----------------------------\n");
        System.out.println(companyDB);
        System.out.println("\n-----------------------------\n");
        assertNotNull(companyDB.getDeletedAt());
    }


    private  Employee createEmployeeWithoutCompanyId() {
        Employee employee = new Employee();
        EmployeeEntity e = employeeRepoService.getLast();
        int lastId = 0;
        if (e != null) lastId =e.getId();

        employee.setId(lastId + 1);

        String[] name = faker.name().nameWithMiddle().split(" ");
        employee.setFirstName(TEST_EMPLOYEE_DATA_PREFIX + name[0]);
        employee.setLastName(name[2]);
        employee.setMiddleName(name[1]);

        employee.setEmail(faker.internet().emailAddress("a" + faker.number().digits(5)));
        employee.setUrl(faker.internet().url());
        employee.setPhone(faker.number().digits(10));
        employee.setBirthdate(Date.valueOf(faker.date().birthday("YYYY-MM-dd")).toString());
        employee.setIsActive(true);

        return employee;
    }

}
