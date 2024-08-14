package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dust.courseRegistration.dto.request.SubjectCreateRequest;
import com.dust.courseRegistration.dto.request.SubjectUpdateRequest;
import com.dust.courseRegistration.dto.response.SubjectResponse;
import com.dust.courseRegistration.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectResponse toSubjectResponse(Subject subject);

    Subject toSubject(SubjectCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateSubjectFromRequest(@MappingTarget Subject subject, SubjectUpdateRequest request);
}
