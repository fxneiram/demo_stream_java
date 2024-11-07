package com.fxneira.demo.stream.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.List;

@Data
public class DualLangMediaRequest {

    @NotBlank(message = "Primary name is required")
    @Size(max = 255, message = "Primary name must be less than 255 characters")
    private String primaryName;

    @NotBlank(message = "Primary language is required")
    @Size(max = 2, min = 2, message = "Primary language must be 2 characters")
    private String primaryLang;

    @NotBlank(message = "Secondary name is required")
    @Size(max = 255, message = "Secondary name must be less than 255 characters")
    private String secondaryName;

    @NotBlank(message = "Secondary language is required")
    @Size(max = 2, min = 2, message = "Secondary language must be 2 characters")
    private String secondaryLang;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, min = 100, message = "Description must be between 100 and 1000 characters")
    private String description;

    private List<String> tags;

    @NotNull(message = "File is required")
    private String base64file;

    @NotNull(message = "File extension is required")
    private String fileExtension;

    @NotNull(message = "File name is required")
    private String fileName;

}
