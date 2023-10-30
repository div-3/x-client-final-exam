package ru.inno.xclient.db;


import ru.inno.xclient.model.db.CompanyEntity;

import java.sql.SQLException;
import java.util.List;

public interface CompanyRepoService {
    List<CompanyEntity> getAll() throws SQLException;

    List<CompanyEntity> getAll(boolean isActive, boolean deleted) throws SQLException;

    List<CompanyEntity> getAll(boolean deleted) throws SQLException;

    CompanyEntity getLast() throws SQLException;

    CompanyEntity getById(int id) throws SQLException;

    int create(String name) throws SQLException;

    int create(String name, String description) throws SQLException;

    int create(CompanyEntity company) throws SQLException;

    void deleteById(int id);

    boolean clean(String prefix) throws SQLException;

    void save(CompanyEntity company);

    CompanyEntity loadEmployeeListToCompany(CompanyEntity company);

}
