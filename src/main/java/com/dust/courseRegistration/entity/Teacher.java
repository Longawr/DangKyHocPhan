package com.dust.courseRegistration.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

import com.dust.courseRegistration.enums.SexType;
import com.dust.courseRegistration.util.NameUtils;

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
public class Teacher {

    @Id
    @Column(name = "username", updatable = false, nullable = false)
    String username;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn(
            name = "username",
            referencedColumnName = "username",
            foreignKey = @ForeignKey(name = "fk_teacher_account"))
    Account account;

    String firstName;
    String lastName;

    @Enumerated(EnumType.STRING)
    SexType sex;

    @OneToMany(
            mappedBy = "teacher",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Course> courses;

    public Teacher(String username) {
        this.username = username;
    }

    public String getFullName() {
        return NameUtils.combineName(this.getFirstName(), this.getLastName());
    }
}
