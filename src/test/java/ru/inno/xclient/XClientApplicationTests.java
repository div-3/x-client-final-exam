package ru.inno.xclient;

import io.qameta.allure.*;
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

import java.sql.SQLException;
import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("x-client API-тесты:")
@Epic("API")
class XClientApplicationTests {

    @Autowired
    private CompanyRepoService companyRepoService;
    @Autowired
    private EmployeeRepoService employeeRepoService;
    @Autowired
    private CompanyService companyApiService;
    @Autowired
    private EmployeeService employeeAPIService;

    @AfterEach
    public void clearData() throws SQLException {
        employeeRepoService.clean("");
        companyRepoService.clean("");
    }

    @Test
    @DisplayName("1. Проверить, что список компаний фильтруется по параметру active")
    @Description("Тест, что по API список компаний фильтруется по параметру active.")
    @Story("Как пользователь, я могу запросить по API список компаний с параметром active = true и active = false")
    @Feature("API getAll(isActive)")
    @Tags({@Tag("API"), @Tag("Company"), @Tag("GetCompany")})  //Теги для JUnit и Allure
    @Severity(SeverityLevel.CRITICAL)    //Важность теста для Allure
    @Owner("Dudorov")
    @Tag("Positive")
    public void shouldApiFilterCompaniesByActive() {

        final List<Company> companiesAPIActive =
                step("1. Получить список активных компаний по API", () ->
                        companyApiService.getAll(true)
                );

        final List<CompanyEntity> companiesDBActive =
                step("2. Получить список активных и неудалённых компаний из BD", () ->
                        companyRepoService.getAll(true, false)
                );

        step("3. Сверить длинны списков активных компаний", () ->
                assertEquals(companiesDBActive.size(), companiesAPIActive.size())
        );

        step("4. Проверить, что списки companyId совпали", () ->
                assertTrue(checkCompaniesListsAPIAndDBEqualsById(companiesAPIActive, companiesDBActive))
        );

        final List<Company> companiesAPIInactive =
                step("5. Получить список НЕактивных компаний по API", () ->
                        companyApiService.getAll(false)
                );

        final List<CompanyEntity> companiesDBInactive =
                step("6. Получить список НЕактивных и неудалённых компаний компаний из BD", () ->
                        companyRepoService.getAll(false, false)
                );

        step("7. Сверить длинны списков НЕактивных компаний", () ->
                assertEquals(companiesDBInactive.size(), companiesAPIInactive.size())
        );

        step("8. Проверить, что списки companyId совпали", () -> {
            assertTrue(checkCompaniesListsAPIAndDBEqualsById(companiesAPIInactive, companiesDBInactive));
        });
    }

    private boolean checkCompaniesListsAPIAndDBEqualsById(List<Company> companiesAPI, List<CompanyEntity> companiesDB) {
        //Получить списки companyId из списков активных компаний, для простоты сравнения
        final List<Integer> companiesIdApi = companiesAPI.stream().map(Company::getId).toList();
        final List<Integer> companiesIdDB = companiesDB.stream().map(CompanyEntity::getId).toList();
        return companiesIdApi.containsAll(companiesIdDB) && companiesIdDB.containsAll(companiesIdApi);
    }

    @Test
    @DisplayName("2. Проверить создание сотрудника в несуществующей компании")
    @Description("Тест, что по API нельзя создать сотрудника для несуществующей в DB компании.")
    @Story("Как администратор, я не могу по API создать сотрудника для несуществующей в DB компании")
    @Feature("API create(companyId)")
    @Tags({@Tag("API"), @Tag("Employee"), @Tag("CreateEmployee")})  //Теги для JUnit и Allure
    @Severity(SeverityLevel.CRITICAL)    //Важность теста для Allure
    @Owner("Dudorov")
    @Tag("Negative")
    public void shouldNotCreateEmployeeToAbsentCompany() throws SQLException, InterruptedException {
        step("1.Авторизоваться по API", () -> {
            employeeAPIService.logIn("", "");
        });

        Employee employee =
                step("2. Создать объект Employee", () -> {
                            Employee e = employeeAPIService.generateEmployee();
                            e.setId(employeeRepoService.getLast().getId() + 1);
                            return e;
                        }
                );

        int lastCompanyId =
                step("3. Получить ID последней созданной компании из DB", () ->
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
    @Description("Тест, что по API getAllByCompanyId(int companyId) и getById(int id) выдача фильтруется по параметру isActive=true.")
    @Story("Как пользователь, я могу запросить по API getAllByCompanyId(int companyId) и getById(int id) только пользователей с параметром isActive = true")
    @Feature("API getAllByCompanyId(int companyId) и getById(int id)")
    @Tags({@Tag("API"), @Tag("Employee"), @Tag("GetEmployee")})  //Теги для JUnit и Allure
    @Severity(SeverityLevel.CRITICAL)    //Важность теста для Allure
    @Owner("Dudorov")
    @Tag("Positive")
    public void shouldNotGetNonActiveEmployee() throws SQLException {
        int companyId =
                step("1. Создать Company в DB", () -> companyRepoService.create(""));

        EmployeeEntity employee =
                step("2. Создать Employee в DB", () -> employeeRepoService.create(companyId));

        step("3. У Employee установить isActive = false и сохранить в DB", () -> {
            employee.setActive(false);
            employeeRepoService.save(employee);
        });

        step("4. Проверить, что неактивный сотрудник не отображается:", () ->
                assertAll(
                        () -> step("4.1 В списке при запросе по Company ID через API",
                                () -> assertNull(employeeAPIService.getAllByCompanyId(companyId))),

                        () -> step("4.2 При запросе по ID через API",
                                () -> assertNull(employeeAPIService.getById(employee.getId())))
                )
        );
    }

    @Test
    @DisplayName("4. Проверить, что у удаленной компании проставляется в БД поле deletedAt")
    @Description("Тест, что при удалении компании по API для неё в БД проставляется значение в поле deletedAt.")
    @Story("Как администратор, я могу удалить компанию по API, при этом в БД для неё проставляется значение в поле deletedAt")
    @Feature("API deleteById(id)")
    @Tags({@Tag("API"), @Tag("Company"), @Tag("DeleteCompany")})  //Теги для JUnit и Allure
    @Severity(SeverityLevel.CRITICAL)    //Важность теста для Allure
    @Owner("Dudorov")
    @Tag("Positive")
    public void shouldFillDeletedAtToDeletedCompany() {
        int companyId =
                step("1. Создать Company в DB", () -> companyRepoService.create(""));

        final CompanyEntity companyDBBefore =
                step("2. Получить Company из DB", () -> companyRepoService.getById(companyId));

        step("3. Проверить, что у Company в поле deleted_at значение null",
                () -> assertNull(companyDBBefore.getDeletedAt()));

        step("4. Авторизоваться по API с правами admin", () -> companyApiService.logIn("", ""));

        step("5. Удалить компанию через API", () -> companyApiService.deleteById(companyId));

        step("6. Дождаться обновления DB", () -> Thread.sleep(3000));

        final CompanyEntity companyDBAfter =
                step("7. Получить Company из DB", () -> companyRepoService.getById(companyId));

        step("8. Проверить, что у Company в поле deleted_at значение !null",
                () -> assertNotNull(companyDBAfter.getDeletedAt()));
    }
}
