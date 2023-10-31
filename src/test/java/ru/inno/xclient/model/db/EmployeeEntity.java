package ru.inno.xclient.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "employee", schema = "public", catalog = "x_clients_db_r06g")
public class EmployeeEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "create_timestamp", nullable = false)
    private Timestamp createTimestamp;
    @Column(name = "change_timestamp", nullable = false)
    private Timestamp changeTimestamp;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name", nullable = true)
    private String middleName;
    @Column(name = "phone", nullable = false)
    private String phone;
    @Column(name = "email", nullable = true)
    private String email;
    @Column(name = "avatar_url", nullable = true)
    private String avatarUrl;
    @Column(name = "birthdate", nullable = true)
    private Date birthdate;

    //Связь с внешней таблицей
    @ManyToOne(targetEntity = CompanyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;


    @Override
    public String toString() {
        return "EmployeeEntity{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", createTimestamp=" + createTimestamp +
                ", changeTimestamp=" + changeTimestamp +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", birthdate=" + birthdate +
                ", company=" + company.getId() +        //Чтобы нормально выводить информацию без циркулярной зависимости
                '}';
    }
}
