package ru.inno.xclient.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private Timestamp deletedAt;

    //Связь с внешней таблицей
//    @JsonIgnore //чтобы не попасть на зацикливание при mapping в Jackson. Hibernate нормально переваривает
//    @OneToMany(targetEntity = EmployeeEntity.class, mappedBy = "companyId", fetch = FetchType.LAZY)
    @ToString.Exclude
    @OneToMany(targetEntity = EmployeeEntity.class, mappedBy = "company", fetch = FetchType.LAZY)
    private List<EmployeeEntity> employees;
}
