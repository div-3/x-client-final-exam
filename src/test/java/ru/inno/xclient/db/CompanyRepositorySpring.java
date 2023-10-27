package ru.inno.xclient.db;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.inno.xclient.model.db.CompanyEntity;

import java.util.List;

@Repository
public interface CompanyRepositorySpring extends ListCrudRepository<CompanyEntity, Integer> {
    List<CompanyEntity> findAllByIsActiveAndDeletedAtIsNotNull(boolean isActive);
    List<CompanyEntity> findAllByIsActiveAndDeletedAtIsNull(boolean isActive);
    List<CompanyEntity> findAllByDeletedAtIsNotNull();
    List<CompanyEntity> findAllByDeletedAtIsNull();
    void deleteByNameStartingWith(String name);
    List<CompanyEntity> findByNameStartingWith(String name);
    CompanyEntity findFirstByOrderByIdDesc();
}
