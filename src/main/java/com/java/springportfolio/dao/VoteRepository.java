package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository  extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.post =:post and v.user =:currentUser order by v.voteId desc ")
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(@Param("post") Post post, @Param("currentUser") User currentUser);

}
