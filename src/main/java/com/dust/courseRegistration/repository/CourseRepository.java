package com.dust.courseRegistration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Course;
import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.entity.Teacher;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findBySemester(Semester semester);

    boolean existsByIdAndTeacher(int courseId, Teacher tch);

    @Query("SELECT COUNT(cs) FROM Course c JOIN c.courseStudents cs WHERE c.id = :courseId")
    int countStudentByCourseId(@Param("courseId") int courseId);
}
