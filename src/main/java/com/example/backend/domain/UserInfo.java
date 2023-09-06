package com.example.backend.domain;

import com.example.backend.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@NoArgsConstructor
@Getter
@ToString
public class UserInfo extends BaseEntity {
//public class UserInfo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 50, nullable = false)
    private String username;

    @Column(name = "EMAIL", length = 50, nullable = false)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "USERROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;


    @Column(name = "posts", nullable = true)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts;

    public UserInfo(){
        this.posts = new ArrayList<>();
    }

    @Column(name = "state", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean state;

    @Builder
    public UserInfo(String username, String email, String password, Role userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.state = false;
        this.posts = new ArrayList<>();
    }

    public UserInfo addPost(Post post){
        this.posts.add(post);
        return this;
    }

    public UserInfo changeState(boolean isAuthenticated){
        this.state = isAuthenticated;
        return this;
    }

    public UserInfo changeRole(Role role){
        this.userRole = role;
        return this;
    }
}

@Converter
class BooleanToYNConverter implements AttributeConverter<Boolean, String>{
    @Override
    public String convertToDatabaseColumn(Boolean attribute){
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData){
        return "Y".equals(dbData);
    }
}