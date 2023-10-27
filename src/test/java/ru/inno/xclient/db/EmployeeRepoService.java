package ru.inno.xclient.db;


import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.util.List;

public interface EmployeeRepoService {

        List<EmployeeEntity> getAllByCompanyId(int companyId);

        EmployeeEntity getById(int id);

        List<EmployeeEntity> getAllByFirstNameLastNameMiddleName(String firstName, String lastName, String middleName);

        int create(EmployeeEntity e);
        EmployeeEntity create(int companyId);

        int update(EmployeeEntity e);

        void deleteById(int id);
        EmployeeEntity getLast();

        List<EmployeeEntity> getAll();

        boolean deleteAllByCompanyId(int companyId);

        boolean clean(String prefix);

        void save(EmployeeEntity employee);

}
