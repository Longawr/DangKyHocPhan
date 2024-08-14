package com.dust.courseRegistration.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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
public class ClassName {

    @Id
    String id;

    String name;

    @Builder.Default
    int total = 0;

    @Builder.Default
    int maleCount = 0;

    @OneToMany(
            mappedBy = "className",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Student> students;
}
