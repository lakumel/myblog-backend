package com.example.backend.controller;

import com.example.backend.controller.dto.RequestPostDto;
import com.example.backend.controller.dto.ResponsePostDto;
import com.example.backend.controller.dto.ResponsePostListDto;
import com.example.backend.domain.Category;
import com.example.backend.domain.Post;
import com.example.backend.service.CategoryService;
import com.example.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @GetMapping("")
    public ResponseEntity getPosts(@PageableDefault(page = 0, size = 5, sort = ("id"), direction = Sort.Direction.DESC) Pageable pageable){
        Page<Post> allPosts = postService.getAllPosts(pageable);

        if(allPosts == null){
            log.info("저장된 포스트가 하나도 없습니다.");
            return ResponseEntity.ok().build();
        }

        ResponsePostListDto postListDto = new ResponsePostListDto(allPosts);

        return ResponseEntity.ok(postListDto);
    }

    @PostMapping("/new")
    public ResponseEntity createPost(@RequestBody RequestPostDto requestPostDto){

        Post savedPost = postService.savePost(requestPostDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ResponsePostDto> retrievePost(@PathVariable("id") Long id){
        Post post = postService.getPost(id);

        ResponsePostDto dto = new ResponsePostDto(post);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity updatePost(@PathVariable("id") Long id, @RequestBody RequestPostDto requestPostDto){
        String categoryName = requestPostDto.getCategory();
        Category category = categoryService.saveOrFindCategory(categoryName);

        Post post = Post.builder()
                .title(requestPostDto.getTitle())
                .subTitle(requestPostDto.getSubTitle())
                .content(requestPostDto.getContent())
//                .category(category)
                .build();
        post.changeCategory(category);

        postService.updatePost(id, post);

        return ResponseEntity.ok().build();
    }
}

