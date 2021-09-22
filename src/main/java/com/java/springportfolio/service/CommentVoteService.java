package com.java.springportfolio.service;

import com.java.springportfolio.dao.CommentRepository;
import com.java.springportfolio.dao.CommentVoteRepository;
import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.CommentVote;
import com.java.springportfolio.entity.VoteType;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.mapper.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentVoteService implements VoteCategoryService {

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Override
    @Transactional
    public void vote(VoteRequest voteRequest) {
        Comment comment = commentRepository.findById(voteRequest.getCommentId())
                .orElseThrow(() -> new ItemNotFoundException("The comment has not been found"));
        CommentVote commentVoteByPostAndUser = commentVoteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(comment, authService.getCurrentUser()).orElse(null);
        if (commentVoteByPostAndUser == null) {
            createVote(voteRequest, comment);
        } else {
            if (voteRequest.getVoteType().equals(commentVoteByPostAndUser.getVoteType())) {
                revokeVote(voteRequest, comment, commentVoteByPostAndUser);
            } else {
                revokeVote(voteRequest, comment, commentVoteByPostAndUser);
                createVote(voteRequest, comment);
            }
        }
        commentRepository.save(comment);
    }

    public boolean checkVoteType(Comment comment, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<CommentVote> voteForPostByUser =
                    commentVoteRepository.findTopByPostAndUserOrderByVoteIdDesc(comment,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

    @Transactional
    void revokeVote(VoteRequest voteRequest, Comment comment, CommentVote commentVoteByPostAndUser) {
        if (voteRequest.getVoteType().equals(commentVoteByPostAndUser.getVoteType())) {
            changeVoteCountReversed(voteRequest, comment);
        } else {
            changeVoteCount(voteRequest, comment);
        }
        commentVoteRepository.deleteById(commentVoteByPostAndUser.getVoteId());
    }

    private void createVote(VoteRequest voteRequest, Comment comment) {
        changeVoteCount(voteRequest, comment);
        commentVoteRepository.save(voteMapper.mapToCommentVote(voteRequest, authService.getCurrentUser(), comment));
    }

    private void changeVoteCount(VoteRequest voteRequest, Comment comment) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                incrementVoteCount(comment);
                break;
            case DOWNVOTE:
                decrementVoteCount(comment);
                break;
        }
    }

    private void changeVoteCountReversed(VoteRequest voteRequest, Comment comment) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                decrementVoteCount(comment);
                break;
            case DOWNVOTE:
                incrementVoteCount(comment);
                break;
        }
    }

    private void incrementVoteCount(Comment comment) {
        comment.setVoteCount(comment.getVoteCount() + 1);
    }

    private void decrementVoteCount(Comment comment) {
        comment.setVoteCount(comment.getVoteCount() - 1);
    }
}
