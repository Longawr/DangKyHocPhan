package com.dust.courseRegistration.entity;

import java.time.LocalDateTime;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
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
public class CourseStudent {

    @EmbeddedId
    CourseStudentId ids;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    Course course;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", referencedColumnName = "username")
    Student student;

    @Builder.Default
    LocalDateTime registeredAt = LocalDateTime.now();

    public CourseStudent(Course course, Student student, LocalDateTime registedAt) {
        this.ids = new CourseStudentId(student.getUsername(), course.getId());
        this.student = student;
        this.course = course;
        this.registeredAt = registedAt;
    }
}
