package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("select t from Topic t")
    Optional<Page<Topic>> findAllTopics(Pageable pageable);

    @Query("select t from Topic t order by t.posts.size DESC ")
    List<Topic> findAllTopicsOrderByNumberOfPosts();

    Topic findByName(String name);

    Boolean existsByName(String name);
}
