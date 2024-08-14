package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.dust.courseRegistration.dto.request.TeacherCreateRequest;
import com.dust.courseRegistration.dto.request.TeacherUpdateRequest;
import com.dust.courseRegistration.dto.response.TeacherResponse;
import com.dust.courseRegistration.entity.Teacher;
import com.dust.courseRegistration.enums.SexType;

@Mapper(
        componentModel = "spring",
        uses = {AccountMapper.class})
public interface TeacherMapper {

    @Mapping(target = "sex", source = "sex", qualifiedByName = "stringToSexType")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Teacher toTeacher(TeacherCreateRequest request);

    @Mapping(target = "sex", expression = "java(teacher.getSex().name())")
    @Mapping(target = "accountByAccount", source = "account")
    TeacherResponse toTeacherResponse(Teacher teacher);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "sex", source = "sex", qualifiedByName = "stringToSexType")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateTeacherFromRequest(@MappingTarget Teacher teacher, TeacherUpdateRequest request);

    @Named("stringToSexType")
    default SexType stringToSexType(String sex) {
        return SexType.valueOf(sex.toUpperCase());
    }
}
