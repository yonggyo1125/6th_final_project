package com.goggles.user_service.user.domain.model2;

import com.goggles.common.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "P_INSTRUCTORS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Instructor extends BaseTime {

    @EmbeddedId
    private InstructorId id;

    @Embedded
    private UserId userId;

    @Enumerated(EnumType.STRING)
    private InstructorStatus status;

    @Embedded
    private BankAccount bankAccount;


    @Embedded
    private InstructorDescription instructorDescription;

    @Column
    private LocalDateTime approvedAt;

    public static Instructor create(
            UserId userId,
            InstructorDescription instructorDescription
    ) {
        Instructor instructor = new Instructor();
        instructor.id = InstructorId.generate();
        instructor.userId = userId;
        instructor.instructorDescription = instructorDescription;
        instructor.status = InstructorStatus.REQUESTED;
        return instructor;
    }

    public void approve() {
        if (this.status != InstructorStatus.REQUESTED) {
            throw new IllegalStateException("승인 불가 상태");
        }
        this.status = InstructorStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject() {
        if (this.status != InstructorStatus.REQUESTED) {
            throw new IllegalStateException("거절 불가 상태");
        }
        this.status = InstructorStatus.REJECTED;
    }
}
