package ru.inno.xclient.db;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static io.qameta.allure.Allure.step;

@Service
@Transactional
//@Commit
public class CompanyRepoServiceSpringImpl implements CompanyRepoService {

    private final String TEST_COMPANY_DATA_PREFIX = "TS_";
    Faker faker = new Faker(new Locale("RU"));
    private CompanyRepositorySpring repository;
    private EmployeeRepositorySpring employeeRepository;

    @Autowired
    @Lazy       //Без этой аннотации получал циркулярную зависимость
    public CompanyRepoServiceSpringImpl(CompanyRepositorySpring repository, EmployeeRepositorySpring employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<CompanyEntity> getAll() {
        return step("Получить список всех компаний из DB", () ->
                repository.findAll()
        );
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive, boolean deleted) {
        return step("Получить список всех компаний из DB", () -> {
            if (deleted) return repository.findAllByIsActiveAndDeletedAtIsNotNull(isActive);
            return repository.findAllByIsActiveAndDeletedAtIsNull(isActive);
        });
    }

    @Override
    public CompanyEntity getLast() {
        return step("Получить последнюю компанию из DB", () ->
                repository.findFirstByOrderByIdDesc()
        );
    }

    @Override
    public CompanyEntity getById(int id) {
        return step("Получить компанию из DB с id='" + id + "'", () ->
                repository.findById(id).get()
        );
    }

    @Override
    public int create(String name) {
        return step("Создать компанию в DB с name = '" + name + "'", () ->
                create(name, "")
        );
    }

    @Override
    public int create(String name, String description) {
        step("Создать компанию в DB с name = '" + name + "', с description = '" + description + "'");

        CompanyEntity company = new CompanyEntity();

        if (name.isEmpty()) name = faker.company().name();
        company.setName(TEST_COMPANY_DATA_PREFIX + name);

        if (description.isEmpty()) description = faker.company().industry();
        company.setDescription(TEST_COMPANY_DATA_PREFIX + description);

        company.setCreateDateTime(Timestamp.valueOf(LocalDateTime.now()));
        company.setChangedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        company.setActive(true);
        repository.save(company);

        return company.getId();
    }

    @Override
    public void deleteById(int id) {
        step("Удалить компанию в DB с id = '" + id + "'", () -> {
            repository.deleteById(id);
        });
    }

    @Override
    public void clean(String prefix) {
        step("Удалить тестовые компании из DB", () -> {
            String prefixFinal = prefix;
            if (prefixFinal.isEmpty()) prefixFinal = TEST_COMPANY_DATA_PREFIX;
            repository.deleteByNameStartingWith(prefixFinal);
        });
    }

    @Override
    public void save(CompanyEntity company) {
        step("Сохранить в DB компанию с  id = '" + company.getId() + "'", () -> {
            repository.save(company);
        });
    }

    @Override
    public CompanyEntity loadEmployeeListToCompany(CompanyEntity company) {
        return step("Загрузить список всех работников из DB для компании с id='" + company.getId() + "'", () -> {
                    company.setEmployees(employeeRepository.findAllByCompanyId(company.getId()));
                    return company;
                }
        );
    }
}
