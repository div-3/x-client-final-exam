package ru.inno.xclient.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties(ignoreUnknown = false)
public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private int companyId;
    private String email;

    //TODO: 2. Написать BUG-репорт, что при запросе Employee через API поле "url" меняется на "avatar_url"
//    @JsonProperty("avatar_url")
    private String url;
    private String phone;

    //TODO: 3. Написать BUG-репорт на несоответствие формата поля "birthdate" в запросах GET по id сотрудника,
    // GET по id компании ("birthdate": "2023-08-12") и требованиях в Swagger ("birthdate": "2023-08-12T10:55:01.426Z")
    private String birthdate;
    //    @JsonProperty("isActive")
    private boolean isActive;
}
