package com.java.springportfolio.util;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.LoginRequest;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.RefreshTokenRequest;
import com.java.springportfolio.dto.RegisterRequest;
import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.VoteCategory;
import com.java.springportfolio.entity.VoteType;
import com.java.springportfolio.exception.PortfolioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.java.springportfolio.entity.VoteType.DOWNVOTE;
import static com.java.springportfolio.entity.VoteType.UPVOTE;


@Slf4j
@Component
public class DtoValidator {

    public void validateTopicRequest(TopicRequest topicRequest) {
        if (topicRequest == null) {
            throw new PortfolioException("Topic request cannot be null!");
        }
        if (topicRequest.getName() == null || topicRequest.getName().equals(" ")) {
            throw new PortfolioException("Topic name cannot be empty or null!");
        }
    }

    public void validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new PortfolioException("Register request cannot be empty or null!");
        }
        if (registerRequest.getUsername() == null || registerRequest.getUsername().equals(" ")) {
            throw new PortfolioException("User name cannot be empty or null!");
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().equals(" ")) {
            throw new PortfolioException("Password cannot be empty or null!");
        }
        if (registerRequest.getEmail() == null || !(registerRequest.getEmail().contains("@") && registerRequest.getEmail().contains("."))) {
            throw new PortfolioException("Email is invalid!");
        }
    }

    public void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new PortfolioException("Login request cannot be null!");
        }
        if (loginRequest.getUsername() == null || loginRequest.getUsername().equals(" ")) {
            throw new PortfolioException("User name cannot be empty or null!");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().equals(" ")) {
            throw new PortfolioException("Password cannot be empty or null!");
        }
    }

    public void validateRefreshTokenRequest(RefreshTokenRequest refreshTokenRequest) {
        if (refreshTokenRequest == null) {
            throw new PortfolioException("Refresh token request request cannot be null!");
        }
        if (refreshTokenRequest.getRefreshToken() == null || refreshTokenRequest.getRefreshToken().equals(" ")) {
            throw new PortfolioException("Refresh token cannot be empty or null!");
        }
        if (refreshTokenRequest.getUsername() == null || refreshTokenRequest.getUsername().equals(" ")) {
            throw new PortfolioException("User name cannot be empty or null!");
        }
    }

    public void validateCommentRequest(CommentRequest commentRequest) {
        if (commentRequest == null) {
            throw new PortfolioException("Comment request cannot be null!");
        }
        if (commentRequest.getText() == null || commentRequest.getText().equals(" ")) {
            throw new PortfolioException("Comment text cannot be null or empty!");
        } else if (commentRequest.getText().length() > 20) {
            throw new PortfolioException("Comment text is too long!");
        }
        if (commentRequest.getPostId() == null) {
            throw new PortfolioException("Post id cannot be null!");
        }
    }

    public void validatePostRequest(PostRequest postRequest) {
        if (postRequest == null) {
            throw new PortfolioException("Post request cannot be null!");
        }
        if (postRequest.getPostName() == null || postRequest.getPostName().equals(" ")) {
            throw new PortfolioException("Post name cannot be empty or null!");
        } else if (postRequest.getPostName().length() > 20) {
            throw new PortfolioException("Post name is too long!");
        }
        if (postRequest.getTopicId() == null) {
            throw new PortfolioException("Topic id cannot be null!");
        }
        //temporary
        if (postRequest.getDescription() != null && postRequest.getDescription().length() > 30) {
            throw new PortfolioException("Description is too long!");
        }
    }

    public void validateVoteRequest(VoteRequest voteRequest) {
        if (voteRequest == null) {
            throw new PortfolioException("Vote request cannot be null!");
        }
        validateVoteType(voteRequest.getVoteType());
        validateVoteCategory(voteRequest.getVoteCategory());
    }

    private void validateVoteType(VoteType voteType) {
        if (voteType == null) {
            throw new PortfolioException("Vote type cannot be null!");
        }
        if (!voteType.equals(DOWNVOTE) && !voteType.equals(UPVOTE)) {
            throw new PortfolioException("Vote type is invalid!");
        }
    }

    private void validateVoteCategory(VoteCategory voteCategory) {
        if (voteCategory == null) {
            throw new PortfolioException("Vote category cannot be null!");
        }
        if (!voteCategory.equals(VoteCategory.COMMENTVOTE) && !voteCategory.equals(VoteCategory.POSTVOTE)) {
            throw new PortfolioException("Vote category is invalid!");
        }
    }
}
