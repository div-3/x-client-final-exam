package ru.inno.xclient.db;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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
        return repository.findAll();
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive, boolean deleted) {
        if (deleted) return repository.findAllByIsActiveAndDeletedAtIsNotNull(isActive);
        return repository.findAllByIsActiveAndDeletedAtIsNull(isActive);
    }

    @Override
    public List<CompanyEntity> getAll(boolean deleted) throws SQLException {
        if (deleted) return repository.findAllByDeletedAtIsNotNull();
        return repository.findAllByDeletedAtIsNull();
    }

    @Override
    public CompanyEntity getLast() {
        return repository.findFirstByOrderByIdDesc();
    }

    @Override
    public CompanyEntity getById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int create(String name) {
        return create(name, "");
    }

    @Override
    public int create(String name, String description) {
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
    public int create(CompanyEntity company) throws SQLException {
        repository.save(company);
        return company.getId();
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public boolean clean(String prefix) {
        if (prefix.isEmpty()) prefix = TEST_COMPANY_DATA_PREFIX;
        repository.deleteByNameStartingWith(prefix);
        return true;
    }

    @Override
    public void save(CompanyEntity company) {
        repository.save(company);
    }

    @Override
    public CompanyEntity loadEmployeeListToCompany(CompanyEntity company) {
        company.setEmployees(employeeRepository.findAllByCompanyId(company.getId()));
        return company;
    }


}
