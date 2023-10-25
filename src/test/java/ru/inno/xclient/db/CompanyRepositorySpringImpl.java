package ru.inno.xclient.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;

@Service
public class CompanyRepositorySpringImpl implements CompanyRepository {
//    @Autowired
    private final CompanyRepositorySpring repository;
    private final String TEST_COMPANY_NAME_PREFIX = "TS_";

    @Autowired
    public CompanyRepositorySpringImpl(CompanyRepositorySpring repository){
        this.repository = repository;
    }

    @Override
    public List<CompanyEntity> getAll() throws SQLException {
        return repository.findAll();
    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive) throws SQLException {
//        return repository.findAllByIsActive(isActive);
        return null;
    }

    @Override
    public CompanyEntity getLast() throws SQLException {
        return null;
    }

    @Override
    public CompanyEntity getById(int id) throws SQLException {
        return null;
    }

    @Override
    public int create(String name) {
        CompanyEntity company = new CompanyEntity();
        company.setName(TEST_COMPANY_NAME_PREFIX + name);
        company.setCreateDateTime(Timestamp.valueOf(LocalDateTime.now()));
        company.setChangedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        repository.save(company);

        return company.getId();
    }

    @Override
    public int create(String name, String description) throws SQLException {
        return 0;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public boolean clean(String prefix) {
        return false;
    }
}
