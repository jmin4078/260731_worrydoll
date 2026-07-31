package org.example.worrydoll.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // JpaConfig -> @EnableJpaAuditing
@Getter
public abstract class BaseEntity { // 자동으로 생성되는 것들
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(updatable = false)
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}