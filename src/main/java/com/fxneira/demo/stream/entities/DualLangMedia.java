package com.fxneira.demo.stream.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "your_collection") // MongoDB
@Entity
@Table(name = "dual_lang_media") // PostgresSQL
@Data
public class DualLangMedia {

    @Id // For MongoDB
    @jakarta.persistence.Id // For PostgresSQL
    @GeneratedValue
    private UUID id;

    private String primaryName;

    @Column(length = 2)
    private String primaryLang;

    private String secondaryName;

    @Column(length = 2)
    private String secondaryLang;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    // Getters y Setters
}
