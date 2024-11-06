package com.fxneira.demo.stream.services;

import com.fxneira.demo.stream.dtos.DefaultError;
import com.fxneira.demo.stream.dtos.DualLangMediaRequest;
import com.fxneira.demo.stream.messages.Error;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucketName;

    public MinioService(@Value("${minio.url}") String url,
                        @Value("${minio.access-key}") String accessKey,
                        @Value("${minio.secret-key}") String secretKey,
                        @Value("${minio.bucket-name}") String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        this.bucketName = bucketName;
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        // Crear el bucket si no existe
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw e;
        }
    }

    public String uploadFile(DualLangMediaRequest mediaRequest) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, InterruptedException {
        MultipartFile file = mediaRequest.getFile();

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        //

        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String uniqueFileName = UUID.randomUUID().toString();
        String segmentFileNamePattern = "/tmp/" + uniqueFileName + "-%03d" + fileExtension;

        // Use FFmpeg to split the video into 10-second segments
        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", tempFile.getAbsolutePath(), "-c", "copy", "-map", "0", "-segment_time", "10", "-f", "segment", segmentFileNamePattern);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        // Read FFmpeg output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg process failed with exit code " + exitCode);
        }

        // Upload each segment to Minio
        int segmentIndex = 0;
        while (true) {
            File segmentFile = new File(String.format("/tmp/" + uniqueFileName + "-%03d" + fileExtension, segmentIndex));
            if (!segmentFile.exists()) {
                break;
            }

            try (InputStream inputStream = new FileInputStream(segmentFile)) {
                String segmentFileName = uniqueFileName + "-" + segmentIndex + fileExtension;
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(segmentFileName)
                        .stream(inputStream, segmentFile.length(), -1)
                        .contentType("application/octet-stream")
                        .build());
            }

            segmentFile.delete();
            segmentIndex++;
        }

        return uniqueFileName;
    }

    public InputStream downloadFile(String objectKey) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectKey)
                .build());
    }
}