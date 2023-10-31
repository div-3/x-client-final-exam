package ru.inno.xclient.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private int companyId;
    private String email;
    private String url;
    private String phone;
    private String birthdate;
    private boolean isActive;
}
