package ru.inno.xclient.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class CompanyEntity implements Serializable {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("isActive")
    private boolean isActive;
    private Timestamp createDateTime;
    private Timestamp changedTimestamp;
    private String name;
    private String description;
    private Timestamp deletedAt;

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
