package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p order by p.createdDate DESC ")
    Optional<List<Post>> findAllPostsOrderByCreatedDate(Pageable pageable);

    @Query("select p from Post p order by p.voteCount DESC ")
    Optional<List<Post>> findAllPostsOrderByNumberOfVotes(Pageable pageable);

    Boolean existsByPostName(String name);

    Optional<Post> findByPostName(String postName);

    @Query("select p from Post p where p.topic.name = :name")
    Optional<List<Post>> findAllPostsByTopic(String name);
}
