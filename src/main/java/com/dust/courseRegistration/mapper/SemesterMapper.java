package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dust.courseRegistration.dto.request.SemesterCreateRequest;
import com.dust.courseRegistration.dto.response.SemesterResponse;
import com.dust.courseRegistration.entity.Semester;

@Mapper(componentModel = "spring")
public interface SemesterMapper {

    @Mapping(target = "ids.id", source = "id")
    @Mapping(target = "ids.year", source = "year")
    @Mapping(target = "startAt", source = "dateStart")
    @Mapping(target = "endAt", source = "dateEnd")
    Semester toSemester(SemesterCreateRequest request);

    @Mapping(target = "id", source = "ids.id")
    @Mapping(target = "year", source = "ids.year")
    @Mapping(target = "dateStart", source = "startAt")
    @Mapping(target = "dateEnd", source = "endAt")
    SemesterResponse toSemesterResponse(Semester semester);

    @Mapping(target = "ids.id", source = "id")
    @Mapping(target = "ids.year", source = "year")
    @Mapping(target = "startAt", source = "dateStart")
    @Mapping(target = "endAt", source = "dateEnd")
    Semester toSemester(SemesterResponse response);
}
