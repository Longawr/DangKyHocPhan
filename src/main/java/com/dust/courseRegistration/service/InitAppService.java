package com.dust.courseRegistration.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dust.courseRegistration.dto.request.AccountCreateRequest;
import com.dust.courseRegistration.dto.request.RoleCreateRequest;
import com.dust.courseRegistration.dto.request.SemesterCreateRequest;
import com.dust.courseRegistration.dto.request.TeacherCreateRequest;
import com.dust.courseRegistration.enums.RoleType;
import com.dust.courseRegistration.enums.SexType;
import com.dust.courseRegistration.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InitAppService {

    AccountService accService;
    RoleService roleService;
    TeacherService tchService;
    SemesterService semService;

    @Transactional
    public void initializeApplication(String adminUsername, String adminPassword) {
        try {
            if (!accService.existAccountByRole(RoleType.ADMIN)) {
                var adminRole = new RoleCreateRequest(RoleType.ADMIN.name(), "Admin", null);
                roleService.createRole(adminRole);
                var gvRole = new RoleCreateRequest(RoleType.GV.name(), "Giảng Viên", null);
                roleService.createRole(gvRole);
                var svRole = new RoleCreateRequest(RoleType.SV.name(), "Sinh Viên", null);
                roleService.createRole(svRole);

                var roles = List.of(RoleType.ADMIN.name(), RoleType.GV.name(), RoleType.SV.name());
                var adminAccount = new AccountCreateRequest(adminUsername, adminPassword, roles);
                accService.createAccount(adminAccount);
                log.warn(
                        "{} user has been created with default password: {}, please change it",
                        adminUsername,
                        adminPassword);

                var adminTeacher = new TeacherCreateRequest(adminAccount.username(), "Admin", null, SexType.NAM.name());
                tchService.createTeacher(adminTeacher);
                log.info("Admin info created");
            }

            var sem = SemesterCreateRequest.of(LocalDate.now());
            if (!semService.existSemester(sem.id(), sem.year())) semService.createSemester(sem);

        } catch (AppException e) {
            log.info("Initializing application service: {}", e.getMessage());
        } catch (DataAccessException ex) {
            log.error("An error occurred while initializing the application:", ex);
        }
    }
}
