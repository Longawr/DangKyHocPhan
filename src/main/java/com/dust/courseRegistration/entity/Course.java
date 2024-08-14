package com.dust.courseRegistration.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "semester_id", referencedColumnName = "id"),
        @JoinColumn(name = "semester_year", referencedColumnName = "year")
    })
    Semester semester;

    String name;
    String room;
    String day;
    String shift;
    int maxSlot;

    @Builder.Default
    int registerSlot = 0;

    @ManyToOne
    @JoinColumn(name = "SubjectId", referencedColumnName = "id")
    Subject subject;

    @ManyToOne
    @JoinColumn(name = "teacherId", referencedColumnName = "username")
    Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CourseStudent> courseStudents;

    @ManyToMany
    Set<RegistrationPeriod> periods;
}
