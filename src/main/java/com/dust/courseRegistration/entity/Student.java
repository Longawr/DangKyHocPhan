package com.dust.courseRegistration.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.Past;

import com.dust.courseRegistration.enums.SexType;

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
@Entity
public class Student {

    @Id
    @Column(name = "username", updatable = false, nullable = false)
    String username;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn(
            name = "username",
            referencedColumnName = "username",
            foreignKey = @ForeignKey(name = "fk_student_account"))
    Account account;

    String firstName;
    String lastName;

    @Enumerated(EnumType.STRING)
    SexType sex;

    @Past(message = "Date of birth must be a date in the past")
    LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "class_id", foreignKey = @ForeignKey(name = "fk_student_class"))
    ClassName className;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CourseStudent> courseStudents;
}
