package ru.inno.xclient;

import io.qameta.allure.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
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
@DisplayName("x-client API-тесты:")
@Epic("API")
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

    //    @AfterEach
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

    //    @Test
    public void companyServiceTest() throws SQLException {
//        int id = companyRepoService.getLast().getId();

//        System.out.println(companyApiService.getById(471));
//        System.out.println(companyApiService.getAll().stream().map(c->c.getId()).toList());
        companyApiService.logIn("", "");
        int id = companyApiService.create("kimba");
        System.out.println("\n--------------------------------------\n");
        companyApiService.changeStatus(id, true);
        System.out.println(companyRepoService.getById(id));
        companyApiService.changeStatus(id, false);
        System.out.println(companyRepoService.getById(id));
        System.out.println("\n--------------------------------------\n");
    }

    @Test
    @DisplayName("1. Проверить, что список компаний фильтруется по параметру active")
    @Description("Тест, что по API список компаний фильтруется по параметру active.")
    @Story("Как пользователь, я могу запросить по API список пользователей с параметром active = true и active = false")
    @Feature("API getAll(isActive)")
    @Tags({@Tag("API"), @Tag("Company"), @Tag("GetCompany"), @Tag("TestRun")})  //Теги для JUnit и Allure
    @Severity(SeverityLevel.CRITICAL)    //Важность теста для Allure
    @Owner("Dudorov")
    @Tag("Positive")
    public void shouldApiFilterCompaniesByActive() {

        final List<Company> companiesAPIActive = step("1. Получить список активных компаний по API", () ->
                companyApiService.getAll(true)
        );

        final List<CompanyEntity> companiesDBActive = step("2. Получить список активных и неудалённых компаний из BD", () ->
                companyRepoService.getAll(true, false)
        );

        step("3. Сверить длинны списков активных компаний", () ->
                assertEquals(companiesDBActive.size(), companiesAPIActive.size())
        );

        step("4. Проверить, что списки companyId совпали", () ->
            assertTrue(checkCompaniesListsAPIAndDBEquals(companiesAPIActive, companiesDBActive))
        );

        final List<Company> companiesAPIInactive = step("5. Получить список НЕактивных компаний по API", () ->
                companyApiService.getAll(false)
        );

        final List<CompanyEntity> companiesDBInactive = step("6. Получить список НЕактивных и неудалённых компаний компаний из BD", () ->
                companyRepoService.getAll(false, false)
        );

        step("7. Сверить длинны списков НЕактивных компаний", () ->
                assertEquals(companiesDBInactive.size(), companiesAPIInactive.size())
        );

        step("8. Проверить, что списки companyId совпали", () -> {
            assertTrue(checkCompaniesListsAPIAndDBEquals(companiesAPIInactive, companiesDBInactive));
        });
    }

    private boolean checkCompaniesListsAPIAndDBEquals(List<Company> companiesAPI, List<CompanyEntity> companiesDB) {
        //Получить списки companyId из списков активных компаний, для простоты сравнения
        final List<Integer> companiesIdApi = companiesAPI.stream().map(Company::getId).toList();
        final List<Integer> companiesIdDB = companiesDB.stream().map(CompanyEntity::getId).toList();
        return companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi);
    }

    @Test
    @DisplayName("2. Проверить создание сотрудника в несуществующей компании")
    public void shouldNotCreateEmployeeToAbsentCompany() throws SQLException, InterruptedException {
        step("1.Авторизоваться по API", () -> {
            employeeAPIService.logIn("", "");
        });

        Employee employee = step("2. Создать объект Employee", () ->
                createEmployeeWithoutCompanyId()
        );

        int lastCompanyId = step("3. Получить ID последней созданной компании из DB", () ->
                companyRepoService.getLast().getId()
        );

        step("4. Установить для Employee номер несуществующей компании", () -> {
            employee.setCompanyId(lastCompanyId + 100);
        });

        step("5. Проверить, что при попытке создания Employee через API выбрасывается исключение", () -> {
            assertThrows(AssertionError.class, () -> {
                employeeAPIService.create(employee);
            });
        });

        step("6. Подождать обновления в DB", () -> {
            Thread.sleep(3000);
        });

        step("7. Проверить, что Employee с тестовыми данными нет в DB", () -> {
            assertEquals(0, employeeRepoService.getAllByFirstNameLastNameMiddleName(
                    employee.getFirstName(), employee.getLastName(), employee.getMiddleName()).size());
        });
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
