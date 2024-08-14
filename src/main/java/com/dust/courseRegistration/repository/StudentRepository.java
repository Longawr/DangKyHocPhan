package com.dust.courseRegistration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("SELECT s FROM Student s WHERE s.className.id = :classId")
    List<Student> findByClassId(@Param("classId") String classId);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s "
            + "WHERE s.username = :username AND s.className.id = :classId")
    boolean existsByUsernameAndClassId(@Param("username") String username, @Param("classId") String classId);
}
