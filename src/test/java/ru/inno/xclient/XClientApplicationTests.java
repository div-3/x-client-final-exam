package ru.inno.xclient;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.inno.xclient.api.CompanyService;
import ru.inno.xclient.api.EmployeeService;
import ru.inno.xclient.db.CompanyRepoService;
import ru.inno.xclient.db.EmployeeRepoService;
import ru.inno.xclient.model.api.Company;
import ru.inno.xclient.model.api.Employee;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class XClientApplicationTests {

    private final String TEST_EMPLOYEE_DATA_PREFIX = "TS_";
    @Autowired
    private CompanyRepoService companyRepoService;
    @Autowired
    private EmployeeRepoService employeeRepoService;
    @Autowired
    private CompanyService companyApiService;
    @Autowired
    private EmployeeService employeeAPIService;
    private Faker faker = new Faker(new Locale("RU"));

    @AfterEach
    public void clearData() throws SQLException {
        employeeRepoService.clean("");
        companyRepoService.clean("");
    }

    //    @Test
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
        step("1. Получить список активных компаний по API");
        //1. Получить список активных компаний по API
        List<Company> companiesAPI = companyApiService.getAll(true); //.stream().forEach(c -> System.out.println("id " + c.getId() + " name: " + c.getName()));

        step("2. Получить список активных компаний из BD");
        //2. Получить список активных компаний из BD
        List<CompanyEntity> companiesDB = companyRepoService.getAll(true, false);   //Активные и неудалённые компании

        //3. Сверить длинны списков
        assertEquals(companiesDB.size(), companiesAPI.size());

        //4. Получить списки companyId из списков активных компаний, для простоты сравнения
        List<Integer> companiesIdApi = companiesAPI.stream().map(c -> c.getId()).toList();
        List<Integer> companiesIdDB = companiesDB.stream().map(c -> c.getId()).toList();
//        System.out.println("API: " + companiesIdApi + " " + companiesIdApi.size());
//        System.out.println("DB: " + companiesIdDB + " " + companiesIdDB.size());

        //5. Проверить, что списки companyId совпали
        assertTrue(companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi));
//        assertEquals(companiesIdDB, companiesIdApi);      //Корректно не проверяет, т.к. элементы в списках могут быть в разном порядке

        //6. Получить список НЕактивных компаний по API
        companiesAPI = companyApiService.getAll(false); //.stream().forEach(c -> System.out.println("id " + c.getId() + " name: " + c.getName()));

        //7. Получить список НЕактивных компаний из BD
        companiesDB = companyRepoService.getAll(false, false);   //Активные и неудалённые компании

        //8. Проверить длинны списков
        assertEquals(companiesDB.size(), companiesAPI.size());

        //9. Получаем списки companyId из списков НЕактивных компаний, для простоты сравнения
        companiesIdApi = companiesAPI.stream().map(c -> c.getId()).toList();
        companiesIdDB = companiesDB.stream().map(c -> c.getId()).toList();
//        System.out.println("API: " + companiesIdApi + " " + companiesIdApi.size());
//        System.out.println("DB: " + companiesIdDB + " " + companiesIdDB.size());

        //10. Проверить, что списки companyId совпали
        assertTrue(companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi));
    }

    @Test
    @DisplayName("2. Проверить создание сотрудника в несуществующей компании")
    public void shouldNotCreateEmployeeToAbsentCompany() throws SQLException, InterruptedException {
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
        assertThrows(AssertionError.class, () -> {
            employeeAPIService.create(employee);
        });

        //6. Подождать обновления в DB
        Thread.sleep(3000);

        //7. Проверить, что Employee с тестовыми данными нет в DB
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

        //3. У Employee установить isActive = false и сохранить в DB
        employee.setActive(false);
        employeeRepoService.save(employee);

        //4. Проверить, что неактивный сотрудник не отображается в списке при запросе по Company ID через API
        assertNull(employeeAPIService.getAllByCompanyId(companyId));

        //5. Проверить, что неактивный сотрудник не отображается при запросе по ID через API
        assertNull(employeeAPIService.getById(employee.getId()));
    }

    @Test
    @DisplayName("4. Проверить, что у удаленной компании проставляется в БД поле deletedAt")
    public void shouldFillDeletedAtToDeletedCompany() throws SQLException, InterruptedException {
        //1. Создать Company в DB
        int companyId = companyRepoService.create("");

        //2. Получить Company из DB
        CompanyEntity companyDB = companyRepoService.getById(companyId);

        //3. Проверить, что у Company в поле deleted_at значение null
        assertNull(companyDB.getDeletedAt());

        //4. Авторизоваться по API с правами admin
        companyApiService.logIn("", "");

        //5. Удалить компанию через API
        companyApiService.deleteById(companyId);

        //6. Дождаться обновления DB
        Thread.sleep(3000);

        //7. Получить Company из DB
        companyDB = companyRepoService.getById(companyId);

        //8. Проверить, что у Company в поле deleted_at значение !null
        assertNotNull(companyDB.getDeletedAt());
    }

    private Employee createEmployeeWithoutCompanyId() {
        Employee employee = new Employee();
        EmployeeEntity e = employeeRepoService.getLast();
        int lastId = 0;
        if (e != null) lastId = e.getId();

        employee.setId(lastId + 1);

        String[] name = faker.name().nameWithMiddle().split(" ");
        employee.setFirstName(TEST_EMPLOYEE_DATA_PREFIX + name[0]);
        employee.setLastName(name[2]);
        employee.setMiddleName(name[1]);

        employee.setEmail(faker.internet().emailAddress("a" + faker.number().digits(5)));
        employee.setUrl(faker.internet().url());
        employee.setPhone(faker.number().digits(10));
        employee.setBirthdate(Date.valueOf(faker.date().birthday("YYYY-MM-dd")).toString());
        employee.setActive(true);

        return employee;
    }

}
