package com.java.springportfolio.dao;

import com.java.springportfolio.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {

    @Query("select cv from CommentVote cv where cv.comment =:comment and cv.user =:currentUser order by cv.voteId desc ")
    Optional<CommentVote> findTopByPostAndUserOrderByVoteIdDesc(Comment comment, User currentUser);
}
