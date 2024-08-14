package com.dust.courseRegistration.mapperTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dust.courseRegistration.dto.request.TeacherCreateRequest;
import com.dust.courseRegistration.dto.response.TeacherResponse;
import com.dust.courseRegistration.entity.Teacher;
import com.dust.courseRegistration.enums.SexType;
import com.dust.courseRegistration.mapper.TeacherMapper;

@SpringBootTest
public class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void contextLoads() {
        assertNotNull(teacherMapper);
    }

    @Test
    public void testToTeacherCreateRequest() {
        Teacher teacher = Teacher.builder()
                .username("john.doe")
                .firstName("John")
                .lastName("Doe")
                .sex(SexType.NAM)
                .build();

        TeacherResponse teacherResponse = teacherMapper.toTeacherResponse(teacher);

        assertThat(teacherResponse).isNotNull();
        assertThat(teacherResponse.getUsername()).isEqualTo("john.doe");
        assertThat(teacherResponse.getFirstName()).isEqualTo("John");
        assertThat(teacherResponse.getLastName()).isEqualTo("Doe");
        assertThat(teacherResponse.getSex()).isEqualTo(SexType.NAM.name());
    }

    @Test
    public void testToTeacher() {
        TeacherCreateRequest teacherCreateRequest =
                new TeacherCreateRequest("john.doe", "John", "Doe", SexType.NAM.name());

        Teacher teacher = teacherMapper.toTeacher(teacherCreateRequest);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getUsername()).isEqualTo("john.doe");
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getSex()).isEqualTo(SexType.NAM);
    }
}
