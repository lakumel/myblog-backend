package com.example.backend.controller;

import com.example.backend.domain.FileName;
import com.example.backend.exception.FileSaveFailException;
import com.example.backend.service.FileNameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.path}")
    private String filePath;
    private final FileNameService fileNameService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) throws URISyntaxException {

        if(multipartFile.isEmpty()){
            log.info("File is Empty");
            throw new FileSaveFailException("파일이 비어있습니다.");
        }

        String storedFileName = fileNameService.saveFile(multipartFile);
        URI uri = new URI(request.getRequestURI());

        return ResponseEntity.created(uri).body(storedFileName);
    }

    @GetMapping("/{storedFileName}")
    public Resource getFile(@PathVariable("storedFileName") String storedFileName) throws MalformedURLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("file:");
        stringBuilder.append(filePath);
        stringBuilder.append("/");
        stringBuilder.append(storedFileName);
        log.info("stored File Path : {}", stringBuilder.toString());
        UrlResource urlResource = new UrlResource(stringBuilder.toString());

        return urlResource;
    }
}
