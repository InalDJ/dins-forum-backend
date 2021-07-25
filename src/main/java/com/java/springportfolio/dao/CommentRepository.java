package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c order by c.createdDate DESC ")
    Optional<List<Comment>> findAllCommentsOrderByCreatedDate();

    @Query("select c from Comment c order by c.voteCount DESC ")
    Optional<List<Comment>> findAllCommentsOrderByNumberOfVotes();
}
