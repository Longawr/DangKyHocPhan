package com.dust.courseRegistration.dto.request;

public record CourseUpdateRequest(
        int semesterId,
        int semesterYear,
        String name,
        String room,
        String day,
        String period,
        int maxSlot,
        String subjectId,
        String teacherId) {}
