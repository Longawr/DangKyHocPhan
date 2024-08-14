package com.dust.courseRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.ClassName;
import com.dust.courseRegistration.enums.SexType;

@Repository
public interface ClassNameRepository extends JpaRepository<ClassName, String> {

    @Query("SELECT COUNT(s) FROM ClassName c JOIN c.students s WHERE c.id = :classId")
    int countStudentsByClassId(@Param("classId") String classId);

    @Query("SELECT COUNT(s) FROM ClassName c JOIN c.students s WHERE c.id = :classId AND s.sex = :gender")
    int countGenderStudentsByClassId(@Param("classId") String classId, @Param("gender") SexType gender);
}
