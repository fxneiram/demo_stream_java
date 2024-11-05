package com.fxneira.demo.stream.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.UUID;

@Document(collection = "dual_lang_media_details")
@Data
public class DualLangMediaDetail {

    @Id
    private UUID id;

    private String primaryName;

    private String primaryLang;

    private String secondaryName;

    private String secondaryLang;

    private String description;

    private ArrayList<String> tags;

    private ArrayList<String> clips;

}
