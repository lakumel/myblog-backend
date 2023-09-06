package com.example.backend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@ToString
public class Category extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CATEGORY_NAME", length = 50, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

//    public Category addPost(Post post){
////        if(posts == null){
////            posts = new ArrayList<>();
////        }
//        posts.add(post);
//        return this;
//    }
    @Builder
    public Category(String name){
        this.name = name;
        this.posts = new ArrayList<>();
    }
}
