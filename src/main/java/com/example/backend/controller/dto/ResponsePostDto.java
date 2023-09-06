package com.example.backend.controller.dto;

import com.example.backend.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResponsePostDto {

    private Long id;
    private String title;
    private String subTitle;
    private List<String> storedFileName;
    private String content;
    private String author;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime lastModifiedTime;

    public ResponsePostDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.subTitle = post.getSubTitle();
        this.content = post.getContent();
        this.author = post.getAuthor().getUsername();
        this.category = post.getCategory().getName();
        this.createdTime = post.getCreatedTime();
        this.lastModifiedTime = post.getLastModifiedTime();
    }
}
