package com.fxneira.demo.stream.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DualLangMediaRequest {

    private String primaryName;

    private String primaryLang;

    private String secondaryName;

    private String secondaryLang;

    private String description;

    private List<String> tags;

    private MultipartFile file;

}
