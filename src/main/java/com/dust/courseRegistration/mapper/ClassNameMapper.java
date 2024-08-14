package com.dust.courseRegistration.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dust.courseRegistration.dto.request.ClassnameCreateRequest;
import com.dust.courseRegistration.dto.request.ClassnameUpdateRequest;
import com.dust.courseRegistration.dto.response.ClassNameResponse;
import com.dust.courseRegistration.entity.ClassName;

@Mapper(componentModel = "spring")
public interface ClassNameMapper {

    @Mapping(target = "femaleCount", expression = "java(className.getTotal() - className.getMaleCount())")
    ClassNameResponse toClassNameResponse(ClassName className);

    @Mapping(target = "total", ignore = true)
    @Mapping(target = "maleCount", ignore = true)
    ClassName toClassName(ClassnameCreateRequest request);

    @Mapping(target = "id", ignore = true)
    void updateClassNameFromRequest(@MappingTarget ClassName className, ClassnameUpdateRequest request);
}
