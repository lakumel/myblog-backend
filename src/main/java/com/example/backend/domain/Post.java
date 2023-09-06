package com.example.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "POST_TITLE", length = 100, nullable = false)
    private String title;

    @Column(name = "POST_SUBTITLE")
    private String subTitle;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false, referencedColumnName = "username")
    @CreatedBy
    private UserInfo author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY", nullable = true, referencedColumnName = "CATEGORY_NAME")
    private Category category;

    // ==== 연관관계 편의 메서드 ==== //
    public void changeCategory(Category category){
        this.category = category;
        // 새로운 카테고리에 해당 포스트 추가
        category.getPosts().add(this);
    }

//    public void setAuthor(UserInfo author){
//        this.author = author;
//        author.getPosts().add(this);
//    }

    public Post updatePost(Post post) {
        this.title = post.title;
        this.subTitle = post.subTitle;
        this.content = post.content;
        changeCategory(post.getCategory());
        return this;
    }
}
