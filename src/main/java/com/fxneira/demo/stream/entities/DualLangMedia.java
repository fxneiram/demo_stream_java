package com.fxneira.demo.stream.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "dual_lang_media")
@Data
@EntityListeners(AuditingEntityListener.class)
public class DualLangMedia {

    @Id
    @GeneratedValue
    private UUID id;

    private String primaryName;

    @Column(length = 2)
    private String primaryLang;

    private String secondaryName;

    @Column(length = 2)
    private String secondaryLang;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;
}
