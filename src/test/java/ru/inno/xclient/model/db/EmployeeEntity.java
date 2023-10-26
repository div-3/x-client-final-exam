package ru.inno.xclient.model.db;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

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
//    @Column(name = "company_id", nullable = false)
//    private int companyId;

        //Связь с внешней таблицей
    @ManyToOne(targetEntity = CompanyEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;


    public EmployeeEntity() {
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Timestamp getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(Timestamp changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeEntity employee = (EmployeeEntity) o;
        return id == employee.id && isActive == employee.isActive && Objects.equals(createTimestamp, employee.createTimestamp) && Objects.equals(changeTimestamp, employee.changeTimestamp) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(middleName, employee.middleName) && Objects.equals(phone, employee.phone) && Objects.equals(email, employee.email) && Objects.equals(avatarUrl, employee.avatarUrl) && Objects.equals(birthdate, employee.birthdate) && Objects.equals(company, employee.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isActive, createTimestamp, changeTimestamp, firstName, lastName, middleName, phone, email, avatarUrl, birthdate, company);
    }

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
                ", company=" + company.getId() +
                '}';
    }
}
