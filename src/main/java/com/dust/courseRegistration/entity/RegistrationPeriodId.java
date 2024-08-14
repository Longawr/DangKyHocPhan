package com.dust.courseRegistration.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Embeddable
public class RegistrationPeriodId implements Serializable {
    static final long serialVersionUID = 3656270084243150447L;

    @Embedded
    SemesterId semesterIds;

    @Column(name = "id")
    int id;

    public RegistrationPeriodId(int id, int semesterId, int semesterYear) {
        this.id = id;
        this.semesterIds = new SemesterId(semesterId, semesterYear);
    }
}
