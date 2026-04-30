package org.pgsg.chat.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.pgsg.chat.domain.exception.InvalidTradeIdException;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomId implements Serializable {

    @Column
    private UUID id;


    public static RoomId of(UUID tradeId){
        if(tradeId == null){
            throw new InvalidTradeIdException();
        }
        return new RoomId(tradeId);
    }

}
