package com.fxneira.demo.stream.controllers;

import com.fxneira.demo.stream.dtos.DefaultError;
import com.fxneira.demo.stream.dtos.DefaultSuccess;
import com.fxneira.demo.stream.dtos.Dto;
import com.fxneira.demo.stream.services.MinioService;
import com.fxneira.demo.stream.messages.Error;

import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<Dto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new DefaultError(Error.FILE_NOT_UPLOADED, ""));
            }

            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            String fileNameUploaded = minioService.uploadFile(Objects.requireNonNull(file.getOriginalFilename()), tempFile);

            return ResponseEntity.status(HttpStatus.CREATED).body(new DefaultSuccess("File uploaded successfully: " + fileNameUploaded));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DefaultError(Error.FILE_UPLOAD_FAILED, e.getMessage()));
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("File size exceeds limit! Please upload a file smaller than " + exc.getMaxUploadSize() + " bytes.");
    }


    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) throws IOException {
        try (InputStream inputStream = minioService.downloadFile(filename)) {
            byte[] bytes = inputStream.readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment; filename=" + filename);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (MinioException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
