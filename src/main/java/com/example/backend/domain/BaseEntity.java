package com.example.backend.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity implements Serializable {

    @CreatedDate
    @Column(name = "CREATEDTIME", updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "LASTMODIFIIEDTIME")
    private LocalDateTime lastModifiedTime;
}
