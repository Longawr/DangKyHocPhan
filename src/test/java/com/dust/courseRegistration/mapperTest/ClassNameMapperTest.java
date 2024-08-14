package com.dust.courseRegistration.mapperTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dust.courseRegistration.dto.request.ClassnameCreateRequest;
import com.dust.courseRegistration.dto.response.ClassNameResponse;
import com.dust.courseRegistration.entity.ClassName;
import com.dust.courseRegistration.mapper.ClassNameMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ClassNameMapperTest {

    @Autowired
    private ClassNameMapper clsMapper;

    @Test
    public void testToClassNameResponse() {

        // Create a mock Permission
        String clsId = "CN01";
        ClassName cls = ClassName.builder()
                .id(clsId)
                .name("cntt")
                .total(40)
                .maleCount(20)
                .students(Set.of())
                .build();

        // Perform the mapping
        ClassNameResponse clsResponse = clsMapper.toClassNameResponse(cls);
        log.info("class to classResponse:");
        log.info("cls: {}", cls);
        log.info("clsResponse: {}", clsResponse);

        // Assertions
        assertEquals(cls.getId(), clsResponse.getId());
        assertEquals(cls.getName(), clsResponse.getName());
        assertEquals(cls.getTotal(), clsResponse.getTotal());
        assertEquals(cls.getMaleCount(), clsResponse.getMaleCount());
        assertEquals(cls.getTotal() - cls.getMaleCount(), clsResponse.getFemaleCount());
    }

    @Test
    public void testToClassName() {

        // Create a mock Permission
        String clsId = "CN01";
        var request = new ClassnameCreateRequest(clsId, "cntt");

        // Perform the mapping
        ClassName classname = clsMapper.toClassName(request);
        log.info("request to class:");
        log.info("request: {}", request);
        log.info("classname: {}", classname);

        // Assertions
        assertEquals(request.id(), classname.getId());
        assertEquals(request.name(), classname.getName());
    }
}
