package com.dust.courseRegistration.entity;

import java.time.LocalDate;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

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
public class Semester {

    @EmbeddedId
    SemesterId ids;

    LocalDate startAt;
    LocalDate endAt;

    public Semester(int semesterId, int semesterYear) {
        this.ids = new SemesterId(semesterId, semesterYear);
    }
}
