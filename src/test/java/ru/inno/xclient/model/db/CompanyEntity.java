package ru.inno.xclient.model.db;

import jakarta.persistence.*;
import ru.inno.xclient.model.api.Employee;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "company", schema = "public", catalog = "x_clients_db_r06g")
public class CompanyEntity implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "is_active", nullable = true)
    private boolean isActive;
    @Column(name = "create_timestamp", nullable = true)
    private Timestamp createDateTime;
    @Column(name = "change_timestamp", nullable = true)
    private Timestamp changedTimestamp;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", nullable = true, length = 300)
    private String description;
    @Column(name = "deleted_at", nullable = true)
    private Timestamp deletedAt;

    //Связь с внешней таблицей
//    @JsonIgnore //чтобы не попасть на зацикливание при mapping в Jackson. Hibernate нормально переваривает
    @OneToMany(targetEntity = EmployeeEntity.class, mappedBy = "companyId", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;


    public CompanyEntity() {
    }

    public CompanyEntity(int id, boolean isActive, Timestamp createDateTime, Timestamp changedTimestamp,
                         String name, String description, Timestamp deletedAt) {
        this.id = id;
        this.isActive = isActive;
        this.createDateTime = createDateTime;
        this.changedTimestamp = changedTimestamp;
        this.name = name;
        this.description = description;
        this.deletedAt = deletedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Timestamp createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Timestamp getChangedTimestamp() {
        return changedTimestamp;
    }

    public void setChangedTimestamp(Timestamp changedTimestamp) {
        this.changedTimestamp = changedTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<EmployeeEntity> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeEntity> employees) {
        this.employees = employees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity companyEntity = (CompanyEntity) o;
        return id == companyEntity.id && isActive == companyEntity.isActive
                && Objects.equals(createDateTime, companyEntity.createDateTime)
                && Objects.equals(changedTimestamp, companyEntity.changedTimestamp)
                && Objects.equals(name, companyEntity.name) && Objects.equals(description, companyEntity.description)
                && Objects.equals(deletedAt, companyEntity.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isActive, createDateTime, changedTimestamp, name, description, deletedAt);
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", createDateTime='" + createDateTime + '\'' +
                ", lastChangedDateTime='" + changedTimestamp + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deletedAt='" + deletedAt + '\'' +
                '}';
    }
}
