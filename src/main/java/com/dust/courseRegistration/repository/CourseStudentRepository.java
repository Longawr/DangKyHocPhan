package com.dust.courseRegistration.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.CourseStudent;
import com.dust.courseRegistration.entity.CourseStudentId;
import com.dust.courseRegistration.entity.SemesterId;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, CourseStudentId> {

    @Query("SELECT cs FROM CourseStudent cs "
            + "WHERE cs.ids.studentId = :studentId "
            + "AND cs.course.semester.ids = :semesterId")
    List<CourseStudent> findAllByStudentIdAndSemesterId(
            @Param("studentId") String studentId, @Param("semesterId") SemesterId semesterId);

    @Query("SELECT cs FROM CourseStudent cs WHERE cs.ids.courseId = :courseId ")
    List<CourseStudent> findAllByCourseId(int courseId);

    @Query("SELECT COUNT(cs) FROM CourseStudent cs "
            + "WHERE cs.ids.studentId = :studentId "
            + "AND cs.course.semester.ids = :semesterId")
    int countByStudentIdAndSemesterId(String studentId, SemesterId semesterId);

    @Query("SELECT COUNT(cs) FROM CourseStudent cs WHERE cs.ids.studentId = :studentId")
    int countByStudentId(String studentId);

    @Query("SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END "
            + "FROM CourseStudent cs "
            + "JOIN cs.course c "
            + "JOIN c.periods p "
            + "WHERE cs.ids = :id "
            + "AND :currentDate BETWEEN p.startAt AND p.endAt")
    boolean ExistsByStudentIdAndCourseIdWithActivePeriod(
            @Param("id") CourseStudentId id, @Param("currentDate") LocalDate currentDate);
}
