package com.fxneira.demo.stream.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/hls")
public class HlsController {

    private final ResourceLoader resourceLoader;

    public HlsController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/playlist")
    public Resource getPlaylist() {
        // Cambia la ruta al archivo M3U8 según tu estructura de directorios
        Path path = Path.of("ruta/a/tu/output.m3u8");
        return resourceLoader.getResource("file:" + path.toAbsolutePath());
    }

    @GetMapping("/segment/**")
    public Resource getSegment() {
        // Extraer el segmento del path y devolver el recurso correspondiente
        String segmentPath = "ruta/a/tu/segmento.ts"; // Cambia esto para mapear correctamente
        return resourceLoader.getResource("file:" + segmentPath);
    }
}