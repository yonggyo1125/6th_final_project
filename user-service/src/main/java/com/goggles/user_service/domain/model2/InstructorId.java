package com.goggles.user_service.domain.model2;

import com.goggles.user_service.domain.exception.InvalidInstructorIdException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstructorId {

    @Column(name = "instructor_id", nullable = false, updatable = false)
    private String instructorId;

    private InstructorId(String instructorId){
        validate(instructorId);
        this.instructorId = instructorId;
    }

    public static InstructorId of(UUID instructorId){
        if (instructorId == null) {
            throw new InvalidInstructorIdException(null);
        }
        return new InstructorId(instructorId.toString());
    }

    public static InstructorId generate(){
        return new InstructorId(UUID.randomUUID().toString());
    }

    private void validate(String instructorId){
        if(instructorId == null || instructorId.isBlank()){
            throw new InvalidInstructorIdException(instructorId);
        }
    }
}
