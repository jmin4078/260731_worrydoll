package org.example.worrydoll.entity;


import jakarta.persistence.Entity;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatUser extends BaseEntity {
    private String username;
}
