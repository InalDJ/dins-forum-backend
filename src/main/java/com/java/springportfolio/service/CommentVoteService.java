package com.java.springportfolio.service;

import com.java.springportfolio.dao.CommentRepository;
import com.java.springportfolio.dao.CommentVoteRepository;
import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.CommentVote;
import com.java.springportfolio.entity.VoteType;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentVoteService implements VoteCategoryService {

    private final CommentVoteRepository commentVoteRepository;
    private final CommentRepository commentRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Autowired
    public CommentVoteService(CommentVoteRepository commentVoteRepository,
                              CommentRepository commentRepository, AuthService authService, VoteMapper voteMapper) {
        this.commentVoteRepository = commentVoteRepository;
        this.commentRepository = commentRepository;
        this.authService = authService;
        this.voteMapper = voteMapper;
    }

    @Override
    @Transactional
    public void vote(VoteRequest voteRequest) {
        Comment comment = commentRepository.findById(voteRequest.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("The comment has not been found"));
        Optional<CommentVote> commentVoteByPostAndUser = commentVoteRepository
                .findTopByPostAndUserOrderByVoteIdDesc(comment, authService.getCurrentUser());

        if (commentVoteByPostAndUser.isPresent() && voteRequest.getVoteType().equals(commentVoteByPostAndUser.get().getVoteType())) {
            revokeVote(voteRequest, comment, commentVoteByPostAndUser.get());
        } else {
            createVote(voteRequest, comment);
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

    private void revokeVote(VoteRequest voteRequest, Comment comment, CommentVote commentVoteByPostAndUser) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                decrementVoteCount(comment);
                break;
            case DOWNVOTE:
                incrementVoteCount(comment);
                break;
        }
        commentVoteRepository.deleteById(commentVoteByPostAndUser.getVoteId());
    }

    private void createVote(VoteRequest voteRequest, Comment comment) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                incrementVoteCount(comment);
                break;
            case DOWNVOTE:
                decrementVoteCount(comment);
                break;
        }
        commentVoteRepository.save(voteMapper.mapToCommentVote(voteRequest, authService.getCurrentUser(), comment));
    }

    private void incrementVoteCount(Comment comment) {
        comment.setVoteCount(comment.getVoteCount() + 1);
    }

    private void decrementVoteCount(Comment comment) {
        comment.setVoteCount(comment.getVoteCount() - 1);
    }
}
