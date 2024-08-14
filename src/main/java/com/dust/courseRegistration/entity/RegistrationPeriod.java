package com.dust.courseRegistration.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

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
public class RegistrationPeriod {

    @EmbeddedId
    RegistrationPeriodId ids;

    @ManyToOne
    @MapsId("semesterIds")
    @JoinColumns({
        @JoinColumn(name = "semesterId", referencedColumnName = "id"),
        @JoinColumn(name = "semesterYear", referencedColumnName = "year")
    })
    Semester semester;

    LocalDate startAt;
    LocalDate endAt;

    @ManyToMany
    Set<Course> courses;

    public RegistrationPeriod(int id, int semesterId, int year) {
        this.ids = new RegistrationPeriodId(id, semesterId, year);
    }
}
