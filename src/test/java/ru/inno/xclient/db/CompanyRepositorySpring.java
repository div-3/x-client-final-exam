package ru.inno.xclient.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.xclient.model.db.CompanyEntity;

import java.util.List;

@Repository
public interface CompanyRepositorySpring extends ListCrudRepository<CompanyEntity, Integer> {
//    List<CompanyEntity> findAllByIsActive(boolean isActive);
}
