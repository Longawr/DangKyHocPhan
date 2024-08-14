package com.dust.courseRegistration.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {

    String id;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String sex;
    ClassNameResponse classnameByClassName;
    AccountResponse accountByAccount;
}
