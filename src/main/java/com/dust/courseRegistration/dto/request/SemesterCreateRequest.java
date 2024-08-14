package com.dust.courseRegistration.dto.request;

import java.time.LocalDate;
import java.time.Month;

import jakarta.validation.constraints.NotBlank;

public record SemesterCreateRequest(
        @NotBlank(message = "this field is mandatory") int id,
        @NotBlank(message = "this field is mandatory") int year,
        LocalDate dateStart,
        LocalDate dateEnd) {

    public static SemesterCreateRequest of(LocalDate currentDate) {
        LocalDate startAt = currentDate;
        int currentSemester = 0;
        int monDur = 4;
        int dayDur = 15;
        int offset = 1;

        if (currentDate.getMonthValue() >= Month.SEPTEMBER.getValue()
                && currentDate.getMonthValue() <= Month.DECEMBER.getValue()) {
            offset = 0;
            currentSemester = 1;
            startAt = LocalDate.of(currentDate.getYear(), Month.SEPTEMBER, 1);

        } else if (currentDate.getMonthValue() >= Month.JULY.getValue()) {
            currentSemester = 3;
            startAt = LocalDate.of(currentDate.getYear(), Month.JULY, 1);
            monDur = 2;
            dayDur = 0;

        } else if (currentDate.getMonthValue() >= Month.FEBRUARY.getValue()) {
            currentSemester = 2;
            startAt = LocalDate.of(currentDate.getYear(), Month.FEBRUARY, 1);

        } else {
            currentSemester = 1;
            startAt = LocalDate.of(currentDate.getYear() - 1, Month.SEPTEMBER, 1);
        }
        return new SemesterCreateRequest(
                currentSemester,
                currentDate.getYear() - offset,
                startAt,
                startAt.plusMonths(monDur).plusDays(dayDur));
    }
}
