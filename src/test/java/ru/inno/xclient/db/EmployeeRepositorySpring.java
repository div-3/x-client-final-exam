package ru.inno.xclient.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

import java.util.List;

@Repository
public interface EmployeeRepositorySpring extends ListCrudRepository<CompanyEntity, Integer> {
    void deleteByFirstNameStartingWith(String name);
    List<CompanyEntity> findByFirstNameStartingWith(String name);
}
