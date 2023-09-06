package com.example.backend.domain;

import com.example.backend.domain.enums.Role;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
class PostTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void createPostTest(){
        String title = "title";
        String subTitle = "subTitle";
        String categoryName = "test";
        String content = "content";

        Category category = Category.builder()
                .name(categoryName)
                .build();

        UserInfo userInfo = UserInfo.builder()
                .username("tester")
                .userRole(Role.READ)
                .password("1234")
                .email("test@test.com")
                .build();

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        Post post = Post.builder()
                .title(title)
                .subTitle(subTitle)
                .category(category)
                .author(savedUserInfo)
                .content(content)
                .build();


        Post savedPost = postRepository.save(post);
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getSubTitle()).isEqualTo(subTitle);
        assertThat(savedPost.getContent()).isEqualTo(content);
    }

}