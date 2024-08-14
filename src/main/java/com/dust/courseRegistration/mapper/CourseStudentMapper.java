package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dust.courseRegistration.dto.response.CourseStudentResponse;
import com.dust.courseRegistration.entity.CourseStudent;

@Mapper(
        componentModel = "spring",
        uses = {StudentMapper.class, CourseMapper.class})
public interface CourseStudentMapper {

    @Mapping(source = "student", target = "student")
    @Mapping(source = "course", target = "course")
    @Mapping(source = "registeredAt", target = "dateRegistered")
    CourseStudentResponse toCourseStudentResponse(CourseStudent crsStd);
}
