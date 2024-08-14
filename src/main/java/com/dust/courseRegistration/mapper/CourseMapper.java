package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dust.courseRegistration.dto.request.CourseCreateRequest;
import com.dust.courseRegistration.dto.request.CourseUpdateRequest;
import com.dust.courseRegistration.dto.response.CourseResponse;
import com.dust.courseRegistration.entity.Course;

@Mapper(
        componentModel = "spring",
        uses = {SubjectMapper.class})
public interface CourseMapper {

    @Mapping(target = "semesterId", source = "semester.ids.id")
    @Mapping(target = "year", source = "semester.ids.year")
    @Mapping(target = "subjectBySubjectId", source = "subject")
    @Mapping(
            target = "teacherName",
            expression = "java(course.getTeacher() != null " + "? course.getTeacher().getFullName() " + ": null)")
    @Mapping(target = "period", source = "shift")
    CourseResponse toCourseResponse(Course course);

    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "shift", source = "period")
    @Mapping(target = "registerSlot", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "courseStudents", ignore = true)
    Course toCourse(CourseCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "shift", source = "period")
    @Mapping(target = "registerSlot", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "courseStudents", ignore = true)
    void updateCourseFromRequest(@MappingTarget Course course, CourseUpdateRequest crs);
}
