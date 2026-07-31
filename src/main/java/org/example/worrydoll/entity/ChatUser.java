package org.example.worrydoll.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;


@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED) // Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA
public class ChatUser extends BaseEntity {
    @Column(unique = true)
    private String username;
}