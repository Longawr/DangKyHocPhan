package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dust.courseRegistration.dto.request.RegistrationPeriodCreateRequest;
import com.dust.courseRegistration.dto.response.RegistrationPeriodResponse;
import com.dust.courseRegistration.entity.RegistrationPeriod;

@Mapper(componentModel = "spring")
public interface RegistrationPeriodMapper {

    @Mapping(target = "id", source = "ids.id")
    @Mapping(target = "semesterId", source = "ids.semesterIds.id")
    @Mapping(target = "year", source = "ids.semesterIds.year")
    @Mapping(target = "dateStart", source = "startAt")
    @Mapping(target = "dateEnd", source = "endAt")
    RegistrationPeriodResponse toRegistrationPeriodResponse(RegistrationPeriod rp);

    @Mapping(target = "ids.id", source = "id")
    @Mapping(target = "ids.semesterIds.id", source = "semesterId")
    @Mapping(target = "ids.semesterIds.year", source = "year")
    @Mapping(target = "semester", ignore = true)
    @Mapping(target = "startAt", source = "dateStart")
    @Mapping(target = "endAt", source = "dateEnd")
    @Mapping(target = "courses", ignore = true)
    RegistrationPeriod toRegistrationPeriod(RegistrationPeriodCreateRequest request);
}
