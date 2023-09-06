package com.example.backend.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class RequestPostDto {

    @NotNull
    private String title;
    private String subTitle;
    private String content;
    private String category;
}
