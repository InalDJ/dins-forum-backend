package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("select t from Topic t order by t.createdDate DESC ")
    List<Topic> findAllTopicsOrderByCreatedDate();

    @Query("select t from Topic t order by t.posts.size DESC ")
    List<Topic> findAllTopicsOrderByNumberOfPosts();

    Topic findByName(String name);

    Boolean existsByName(String name);
}
