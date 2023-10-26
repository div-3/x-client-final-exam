package ru.inno.xclient.db;

import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static java.time.LocalTime.now;

@Service
public class CompanyRepoServiceSpringImpl implements CompanyRepoService {

    private final String TEST_COMPANY_DATA_PREFIX = "TS_";
    private CompanyRepositorySpring companyRepository;
    Faker faker = new Faker(new Locale("RU"));

    @Autowired
    @Lazy       //Без этой аннотации получал циркулярную зависимость
    public CompanyRepoServiceSpringImpl(CompanyRepositorySpring repository) {
        this.companyRepository = repository;
    }

    @Override
    public List<CompanyEntity> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive) {
        return companyRepository.findAllByIsActive(isActive);
    }

    @Override
    public CompanyEntity getLast() {
        return null;
    }

    @Override
    public CompanyEntity getById(int id) {
        return companyRepository.findById(id).get();
    }

    @Override
    public int create(String name) {
        return create(name,"");
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
        companyRepository.save(company);

        return company.getId();
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public boolean clean(String prefix) {
        if (prefix.isEmpty()) prefix = TEST_COMPANY_DATA_PREFIX;
        companyRepository.deleteByNameStartingWith(prefix);
        return true;
    }
}
