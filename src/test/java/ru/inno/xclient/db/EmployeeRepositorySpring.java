package ru.inno.xclient.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import ru.inno.xclient.model.db.CompanyEntity;
import ru.inno.xclient.model.db.EmployeeEntity;

public interface EmployeeRepositorySpring extends ListCrudRepository<CompanyEntity, Integer> {
}
