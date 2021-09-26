package com.java.springportfolio.dao;

import com.java.springportfolio.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c order by c.createdDate DESC ")
    Optional<List<Comment>> findAllCommentsOrderByCreatedDate(Pageable pageable);

    @Query("select c from Comment c order by c.voteCount DESC ")
    Optional<List<Comment>> findAllCommentsOrderByNumberOfVotes(Pageable pageable);

    @Query("select c from Comment c where c.post.postId = :postId and c.parentCommentId is null")
    Optional<List<Comment>> findAllCommentsByPost(@Param("postId") Long postId);

    @Query("select c from Comment c where c.post.postId = :postId and c.parentCommentId = :parentCommentId")
    Optional<List<Comment>> findAllCommentsByPostAndParentCommentId(@Param("postId") Long postId, @Param("parentCommentId") Long parentCommentId);

    @Modifying
    @Query("update Comment c set c.subCommentCount = c.subCommentCount + 1 where c.id = :id")
    void incrementSubcommentCount(@Param("id") Long parentCommentId);

    @Modifying
    @Query("update Comment c set c.subCommentCount = c.subCommentCount - 1 where c.id = :id")
    void decrementSubcommentCount(@Param("id") Long parentCommentId);
}
