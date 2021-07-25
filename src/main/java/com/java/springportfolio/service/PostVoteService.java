package com.java.springportfolio.service;

import com.java.springportfolio.dao.PostRepository;
import com.java.springportfolio.dao.VoteRepository;
import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.Vote;
import com.java.springportfolio.entity.VoteType;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.mapper.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostVoteService implements VoteCategoryService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Override
    @Transactional
    public void vote(VoteRequest voteRequest) {
        Post post = postRepository.findById(voteRequest.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("Post has not been found!"));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteRequest.getVoteType())) {
            revokeVote(voteRequest, post, voteByPostAndUser.get());
        } else {
            createVote(voteRequest, post);
        }
        postRepository.save(post);
    }

    private void revokeVote(VoteRequest voteRequest, Post post, Vote voteByPostAndUser) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                decrementVoteCount(post);
                break;

            case DOWNVOTE:
                incrementVoteCount(post);
                break;
        }
        voteRepository.deleteById(voteByPostAndUser.getVoteId());
    }

    private void createVote(VoteRequest voteRequest, Post post) {
        switch (voteRequest.getVoteType()) {
            case UPVOTE:
                incrementVoteCount(post);
                break;
            case DOWNVOTE:
                decrementVoteCount(post);
                break;
        }
        voteRepository.save(voteMapper.mapToPostVote(voteRequest, authService.getCurrentUser(), post));
    }

    private void incrementVoteCount(Post post) {
        post.setVoteCount(post.getVoteCount() + 1);
    }

    private void decrementVoteCount(Post post) {
        post.setVoteCount(post.getVoteCount() - 1);
    }

    public boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
}
