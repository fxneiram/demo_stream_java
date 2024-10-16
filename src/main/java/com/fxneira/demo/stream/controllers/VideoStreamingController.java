package com.fxneira.demo.stream.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/video")
public class VideoStreamingController {

    @Autowired
    private ResourceLoader resourceLoader;

    private final String VIDEO_PATH = "classpath:static/demo.mp4";

    @GetMapping(value = "/stream", produces = "video/mp4")
    public ResponseEntity<InputStreamResource> streamVideo(@RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        Resource videoResource = resourceLoader.getResource(VIDEO_PATH);
        long fileSize = videoResource.contentLength();

        if (rangeHeader == null) {
            // Si no hay cabecera Range, se envía el archivo completo
            InputStreamResource resource = new InputStreamResource(videoResource.getInputStream());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .contentLength(fileSize)
                    .body(resource);
        } else {
            // Si se especifica Range, se procesa para enviar el fragmento correspondiente
            long rangeStart = 0, rangeEnd = fileSize - 1;

            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            if (!ranges[0].isEmpty()) {
                rangeStart = Long.parseLong(ranges[0]);
            }
            if (ranges.length > 1 && !ranges[1].isEmpty()) {
                rangeEnd = Long.parseLong(ranges[1]);
            }

            if (rangeEnd > fileSize - 1) {
                rangeEnd = fileSize - 1;
            }

            long contentLength = rangeEnd - rangeStart + 1;

            InputStream inputStream = videoResource.getInputStream();
            inputStream.skip(rangeStart);

            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);
            headers.setContentLength(contentLength);
            headers.set("Accept-Ranges", "bytes");

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(resource);
        }
    }
}