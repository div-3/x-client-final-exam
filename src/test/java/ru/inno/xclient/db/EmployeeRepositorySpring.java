package ru.inno.xclient.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepositorySpring extends ListCrudRepository<EmployeeEntity, Integer> {
    void deleteByFirstNameStartingWith(String name);

    EmployeeEntity findFirstByOrderByIdDesc();


    List<EmployeeEntity> findByFirstNameStartingWith(String name);

    List<EmployeeEntity> findAllByCompanyId(int id);

    void deleteAllByCompanyId(int id);
}
