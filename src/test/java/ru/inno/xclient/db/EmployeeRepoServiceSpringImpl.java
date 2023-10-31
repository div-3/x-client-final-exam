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


@Service
@Transactional
//@Commit
public class EmployeeRepoServiceSpringImpl implements EmployeeRepoService {
    private final String TEST_EMPLOYEE_DATA_PREFIX = "TS_";
    private final EmployeeRepositorySpring repository;
    Faker faker = new Faker(new Locale("RU"));

    @Autowired
    @Lazy       //Без этой аннотации получал циркулярную зависимость
    public EmployeeRepoServiceSpringImpl(EmployeeRepositorySpring repository) {
        this.repository = repository;
    }

    @Override
    public List<EmployeeEntity> getAllByCompanyId(int companyId) {
        return repository.findAllByCompanyId(companyId);
    }

    @Override
    public EmployeeEntity getById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public List<EmployeeEntity> getAllByFirstNameLastNameMiddleName(String firstName, String lastName, String middleName) {
        return repository.findAllByFirstNameAndLastNameAndMiddleName(firstName, lastName, middleName);
    }

    @Override
    public int create(EmployeeEntity e) {
        repository.save(e);
        return e.getId();
    }

    @Override
    public EmployeeEntity create(int companyId) {
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
        return 0;
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public EmployeeEntity getLast() {
        return repository.findFirstByOrderByIdDesc();
    }

    @Override
    public List<EmployeeEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public boolean deleteAllByCompanyId(int companyId) {
        repository.deleteAllByCompanyId(companyId);
        return true;
    }

    @Override
    public void clean(String prefix) {
        if (prefix.isEmpty()) prefix = TEST_EMPLOYEE_DATA_PREFIX;
        repository.deleteByFirstNameStartingWith(prefix);
    }

    @Override
    public void save(EmployeeEntity employee) {
        repository.save(employee);
    }
}
