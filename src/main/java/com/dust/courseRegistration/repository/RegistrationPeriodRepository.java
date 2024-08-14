package com.dust.courseRegistration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.RegistrationPeriod;
import com.dust.courseRegistration.entity.RegistrationPeriodId;

@Repository
public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, RegistrationPeriodId> {

    @Query("SELECT rp FROM RegistrationPeriod rp"
            + " WHERE rp.ids.semesterIds.id = :semesterId"
            + " AND rp.ids.semesterIds.year = :semesterYear")
    List<RegistrationPeriod> findBySemesterIds(
            @Param("semesterId") int semesterId, @Param("semesterYear") int semesterYear);
}
