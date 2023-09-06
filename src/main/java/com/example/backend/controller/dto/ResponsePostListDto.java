package com.example.backend.controller.dto;

import com.example.backend.domain.Post;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponsePostListDto {
    private List<ResponsePostDto> postList;
    private Integer numOfElements;
    private Long totalElements;
    private Integer pagingSize;
    private Integer pageIndex;
    private Integer pageLimit;

    public ResponsePostListDto(Page<Post> postPage){
        this.postList = postPage.stream()
                .map(ResponsePostDto::new)
                .collect(Collectors.toList());
        this.numOfElements = postPage.getNumberOfElements();
        this.totalElements = postPage.getTotalElements();
        this.pagingSize = postPage.getSize();
        this.pageIndex = postPage.getNumber();
        this.pageLimit = (postPage.getNumberOfElements() / postPage.getSize());
    }
}
