package com.example.domain.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    // 등록일
    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    private LocalDateTime regDate;

    // 수정일
    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate;

}