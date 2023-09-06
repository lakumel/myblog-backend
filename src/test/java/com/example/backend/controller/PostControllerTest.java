package com.example.backend.controller;

import com.example.backend.controller.dto.RequestPostDto;
import com.example.backend.domain.Category;
import com.example.backend.domain.Post;
import com.example.backend.exception.PostSaveFailException;
import com.example.backend.security.service.UserInfoUserDetailsService;
import com.example.backend.security.utils.JwtUtils;
import com.example.backend.service.CategoryService;
import com.example.backend.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
//        (value = PostController.class
//        , excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JsonSecurityConfig.class),
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtSecurityConfig.class)
//})
@WebMvcTest(value = PostController.class)
@ActiveProfiles("local")
//@TestPropertySource("classpath:application-test.yml")
class PostControllerTest {

    @Value("${security.jwt.secret}")
    String secret;
    @Value("${security.jwt.token-validity-in-seconds}")
    Long tokenValidityInMilliseconds;


    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean(name = "userDetailsService")
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @PostConstruct
    public void setup() {
        given(userInfoUserDetailsService.loadUserByUsername(anyString()))
                .willReturn(new User("test", "password", AuthorityUtils.createAuthorityList("ROLE_WRITE")));
    }

    @Test
    @WithMockUser
    void getAllTest() throws Exception {
        List<Page> posts = new ArrayList<>();
        given(postService.getAllPosts(PageRequest.of(0, 3))).willReturn(new PageImpl(posts));

        ResultActions resultActions = mockMvc.perform(get("/api/post"));

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    void saveTest() throws Exception {
        String title = new String("Title");
        String subTitle = new String("subTitle");
        String content = new String("content");
        String beforeCategoryName = new String("beforeCategory");
        String afterCategoryName = new String("afterCategory");

        RequestPostDto requestPostDto = new RequestPostDto();
        requestPostDto.setTitle(title);
        requestPostDto.setSubTitle(subTitle);
        requestPostDto.setContent(content);
        requestPostDto.setCategory(afterCategoryName);

        Category beforeCategory = Category.builder()
                .name(beforeCategoryName)
                .build();

        Category afterCategory = Category.builder()
                .name(afterCategoryName)
                .build();

        Post post = Post.builder()
                .id(1L)
                .title(title)
                .subTitle(subTitle)
                .content(content)
                .category(beforeCategory)
                .build();

        given(categoryService.saveOrFindCategory(afterCategoryName)).willReturn(afterCategory);
        given(postService.savePost(any(RequestPostDto.class))).willReturn(post);

        String requestBody = objectMapper.writeValueAsString(requestPostDto);

        ResultActions resultActions = mockMvc.perform(post("/api/post/new")
                .with(csrf())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isCreated())
                .andDo(print());

        verify(postService).savePost(any(RequestPostDto.class));
    }

    @Test
    @WithUserDetails
    void saveFailTest() throws Exception {
        String title = new String("Title");
        String subTitle = new String("subTitle");
        String content = new String("content");
        String afterCategoryName = new String("afterCategory");

        RequestPostDto requestPostDto = new RequestPostDto();
        requestPostDto.setTitle(title);
        requestPostDto.setSubTitle(subTitle);
        requestPostDto.setContent(content);
        requestPostDto.setCategory(afterCategoryName);

        Category afterCategory = Category.builder()
                .name(afterCategoryName)
                .build();


        given(categoryService.saveOrFindCategory(afterCategoryName)).willReturn(afterCategory);
        given(postService.savePost(any(RequestPostDto.class))).willThrow(PostSaveFailException.class);

        String requestBody = objectMapper.writeValueAsString(requestPostDto);

        ResultActions resultActions = mockMvc.perform(post("/api/post/new")
                .content(requestBody)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(postService).savePost(any(RequestPostDto.class));
    }

    @Test
    @WithUserDetails
    void deletePostTest() throws Exception {
        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(delete("/api/post/delete/" + id)
                .with(csrf()));

        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        verify(postService).deletePost(1L);
    }
}