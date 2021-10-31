package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p")
    Optional<Page<Post>> findAllPosts(Pageable pageable);

    Boolean existsByPostName(String name);

    Optional<Post> findByPostName(String postName);

    @Query("select p from Post p where p.topic.name = :name")
    Optional<Page<Post>> findAllPostsByTopic(@Param("name") String name, Pageable pageable);

    @Query("select p from Post p where p.user.userId = :userId")
    Optional<Page<Post>> findAllPostsByUser(@Param("userId") Long userId, Pageable pageable);
}
