package com.dust.courseRegistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dust.courseRegistration.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {}
