package com.dust.courseRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Semester;
import com.dust.courseRegistration.entity.SemesterId;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, SemesterId> {}
