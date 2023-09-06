package com.example.backend.repository;

import com.example.backend.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findUserInfoByEmail(String email);

    @Query("select u.username from UserInfo u where u.email = :email")
    Optional<String> findUsernameByEmail(String email);

//    @Query("select u from UserInfo u left join fetch u.posts p where u.email = :email")
////    @Query("select u from UserInfo u where u.email = :email")
//    Optional<UserInfo> findAllWithPostByEmail(@Param("email") String email);
}
