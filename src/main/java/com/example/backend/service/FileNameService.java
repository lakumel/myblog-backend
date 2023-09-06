package com.example.backend.service;

import com.example.backend.domain.FileName;
import com.example.backend.repository.FileNameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileNameService {

    @Value("${file.path}")
    private String filePath;

    private final FileNameRepository fileNameRepository;

    private String getStoredFileName(String originalFileName){
        int extStartIndex = originalFileName.lastIndexOf(".");
        String fileName = originalFileName.substring(0, extStartIndex);
        String ext = originalFileName.substring(extStartIndex);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UUID.randomUUID().toString());
        stringBuilder.append(ext);

        return stringBuilder.toString();
    }

    public String saveFile(MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String storedFilename = getStoredFileName(originalFilename);
        String uploadPath = filePath + storedFilename;

        try{
            log.info("file stored at : {}", uploadPath);
            multipartFile.transferTo(Paths.get(uploadPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileName fileName = FileName.builder()
                .originalFileName(originalFilename)
                .storedFileName(storedFilename)
                .build();

        fileNameRepository.save(fileName);

        return storedFilename;
    }

    public void deleteFileByStoredFilename(String storedFilename) throws IOException {
        String storedFilePath = filePath + storedFilename;
        Path path = Paths.get(storedFilePath);
        Files.delete(path);
    }

    private FileName getOriginalFileName(String storedFileName){
        Optional<FileName> optional = fileNameRepository.findFileNameByStoredFileName(storedFileName);

        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new IllegalStateException("파일이 존재하지 않습니다.");
        }
    }
}
