package ru.inno.xclient.db;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static io.qameta.allure.Allure.step;


@Service
@Transactional
//@Commit
public class EmployeeRepoServiceSpringImpl implements EmployeeRepoService {
    private final String TEST_EMPLOYEE_DATA_PREFIX = "TS_";
    private final EmployeeRepositorySpring repository;
    final Faker faker = new Faker(new Locale("RU"));

    @Autowired
    @Lazy       //Без этой аннотации получал циркулярную зависимость
    public EmployeeRepoServiceSpringImpl(EmployeeRepositorySpring repository) {
        this.repository = repository;
    }

    @Override
    public List<EmployeeEntity> getAllByCompanyId(int companyId) {
        return step("Получить из DB всех работников компании с id='" + companyId + "'", () ->
                repository.findAllByCompanyId(companyId)
        );
    }

    @Override
    public EmployeeEntity getById(int id) {
        return step("Получить из DB работника с id='" + id + "'", () ->
                repository.findById(id).get()
        );
    }

    @Override
    public List<EmployeeEntity> getAllByFirstNameLastNameMiddleName(String firstName, String lastName, String middleName) {
        return step("Получить из DB всех работников с ФИО='" + firstName + " " + middleName + " " + lastName, () ->
                repository.findAllByFirstNameAndLastNameAndMiddleName(firstName, lastName, middleName)
        );
    }

    @Override
    public int create(EmployeeEntity e) {
        return step("Сохранить в DB работника с id='" + e.getId() + "'", () -> {
            repository.save(e);
            return e.getId();
        });
    }

    @Override
    public EmployeeEntity create(int companyId) {
        step("Создать в DB работника для компании с id='" + companyId + "'");
        EmployeeEntity employee = new EmployeeEntity();

        String[] name = faker.name().nameWithMiddle().split(" ");
        employee.setFirstName(TEST_EMPLOYEE_DATA_PREFIX + name[0]);
        employee.setLastName(name[2]);
        employee.setMiddleName(name[1]);
        CompanyEntity company = new CompanyEntity();
        company.setId(companyId);
        employee.setCompany(company);

        employee.setEmail(faker.internet().emailAddress("a" + faker.number().digits(5)));
        employee.setAvatarUrl(faker.internet().url());
        employee.setPhone(faker.number().digits(10));

        Timestamp tmp = Timestamp.valueOf(LocalDateTime.now());
        employee.setCreateTimestamp(tmp);
        employee.setChangeTimestamp(tmp);

        employee.setBirthdate(Date.valueOf(faker.date().birthday("YYYY-MM-dd")));
        employee.setActive(true);

        repository.save(employee);
        return employee;
    }

    @Override
    public int update(EmployeeEntity e) {
        return step("Обновить в DB работника с id='" + e.getId() + "'", () -> {
            repository.save(e);
            return e.getId();
        });
    }

    @Override
    public void deleteById(int id) {
        step("Удалить в DB работника с id='" + id + "'", () ->
                repository.deleteById(id)
        );
    }

    @Override
    public EmployeeEntity getLast() {
        return step("Получить последнего работника в DB", () ->
                repository.findFirstByOrderByIdDesc()
        );
    }

    @Override
    public List<EmployeeEntity> getAll() {
        return step("Получить всех работников из DB", () ->
                repository.findAll()
        );
    }

    @Override
    public void deleteAllByCompanyId(int companyId) {
        step("Удалить в DB всех работников компании с id='" + companyId + "'", () ->
                repository.deleteAllByCompanyId(companyId)
        );
    }

    @Override
    public void clean(String prefix) {
        step("Удалить тестовых работников из DB", () -> {
            String prefixFinal = prefix;
            if (prefixFinal.isEmpty()) prefixFinal = TEST_EMPLOYEE_DATA_PREFIX;
            repository.deleteByFirstNameStartingWith(prefixFinal);
        });
    }

    @Override
    public void save(EmployeeEntity employee) {
        step("Сохранить в DB работника с id='" + employee.getId() + "'", () -> {
            repository.save(employee);
        });
    }
}
