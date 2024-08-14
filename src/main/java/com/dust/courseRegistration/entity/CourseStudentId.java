package com.dust.courseRegistration.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseStudentId implements Serializable {
    static final long serialVersionUID = -4266279784303941554L;

    @Column(name = "studentId")
    String studentId;

    @Column(name = "courseId")
    int courseId;
}
