package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.CommentVote;
import com.java.springportfolio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    @Query("select cv from CommentVote cv where cv.comment =:comment and cv.user =:currentUser order by cv.voteId desc ")
    Optional<CommentVote> findTopByPostAndUserOrderByVoteIdDesc(@Param("comment") Comment comment, @Param("currentUser") User currentUser);
}
