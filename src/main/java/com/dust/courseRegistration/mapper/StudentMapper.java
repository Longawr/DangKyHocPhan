package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.dust.courseRegistration.dto.request.StudentCreateRequest;
import com.dust.courseRegistration.dto.request.StudentUpdateRequest;
import com.dust.courseRegistration.dto.response.StudentPublicResponse;
import com.dust.courseRegistration.dto.response.StudentResponse;
import com.dust.courseRegistration.entity.Student;
import com.dust.courseRegistration.enums.SexType;

@Mapper(
        componentModel = "spring",
        uses = {ClassNameMapper.class, AccountMapper.class})
public interface StudentMapper {

    @Mapping(target = "id", source = "username")
    @Mapping(target = "dateOfBirth", source = "birthDate")
    @Mapping(target = "sex", expression = "java(student.getSex().name())")
    @Mapping(target = "classnameByClassName", source = "className")
    @Mapping(target = "accountByAccount", source = "account")
    StudentResponse toStudentResponse(Student student);

    @Mapping(target = "id", source = "username")
    @Mapping(target = "dob", source = "birthDate")
    @Mapping(target = "ClassName", source = "className.id")
    StudentPublicResponse toStudentPublicResponse(Student student);

    @Mapping(target = "birthDate", source = "dob")
    @Mapping(target = "sex", source = "sex", qualifiedByName = "stringToSexType")
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "courseStudents", ignore = true)
    Student toStudent(StudentCreateRequest request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "birthDate", source = "dob")
    @Mapping(target = "sex", qualifiedByName = "stringToSexType")
    @Mapping(target = "className", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "courseStudents", ignore = true)
    void updateStudentFromRequest(@MappingTarget Student student, StudentUpdateRequest request);

    @Named("stringToSexType")
    default SexType stringToSexType(String sex) {
        return SexType.valueOf(sex.toUpperCase());
    }
}
