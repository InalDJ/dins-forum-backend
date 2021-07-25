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

import static com.java.springportfolio.entity.VoteCategory.COMMENTVOTE;
import static com.java.springportfolio.entity.VoteCategory.POSTVOTE;
import static com.java.springportfolio.entity.VoteType.DOWNVOTE;
import static com.java.springportfolio.entity.VoteType.UPVOTE;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Component
public class DtoValidator {

    private static final int TOPIC_NAME_LENGTH = 100;
    private static final int TOPIC_DESCRIPTION_LENGTH = 250;
    private static final int USERNAME_LENGTH = 40;
    private static final int PASSWORD_LENGTH = 40;
    private static final int TEXT_LENGTH = 500;
    private static final int POST_NAME_LENGTH = 100;
    private static final int DESCRIPTION_LENGTH = 500;

    public void validateTopicRequest(TopicRequest topicRequest) {
        if (topicRequest == null) {
            throw new PortfolioException("Topic request cannot be null!");
        }
        validateMandatoryField(topicRequest.getName(), TOPIC_NAME_LENGTH, "Topic name");
        validateMandatoryField(topicRequest.getDescription(), TOPIC_DESCRIPTION_LENGTH, "Topic name");
    }

    public void validateRegisterRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new PortfolioException("Register request cannot be empty or null!");
        }
        validateMandatoryField(registerRequest.getUsername(), USERNAME_LENGTH, "User name");
        validateMandatoryField(registerRequest.getPassword(), PASSWORD_LENGTH, "Password");
        validateEmail(registerRequest.getEmail());
    }

    public void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new PortfolioException("Login request cannot be null!");
        }
        validateMandatoryField(loginRequest.getUsername(), USERNAME_LENGTH, "User name");
        validateMandatoryField(loginRequest.getPassword(), PASSWORD_LENGTH, "Password");
    }

    public void validateRefreshTokenRequest(RefreshTokenRequest refreshTokenRequest) {
        if (refreshTokenRequest == null) {
            throw new PortfolioException("Refresh token request request cannot be null!");
        }
        validateMandatoryField(refreshTokenRequest.getRefreshToken(), "Refresh token");
        validateMandatoryField(refreshTokenRequest.getUsername(), USERNAME_LENGTH, "User name");
    }

    public void validateCommentRequest(CommentRequest commentRequest) {
        if (commentRequest == null) {
            throw new PortfolioException("Comment request cannot be null!");
        }
        validateMandatoryField(commentRequest.getText(), TEXT_LENGTH, "Text");
        // TODO temporary solution
        validateMandatoryField(commentRequest.getPostId().toString(), "Post id");
    }

    public void validatePostRequest(PostRequest postRequest) {
        if (postRequest == null) {
            throw new PortfolioException("Post request cannot be null!");
        }
        validateMandatoryField(postRequest.getPostName(), POST_NAME_LENGTH, "Post name");

        // TODO temporary solution
        validateMandatoryField(postRequest.getTopicId().toString(), "Topic id");
        validateMandatoryField(postRequest.getDescription(), DESCRIPTION_LENGTH, "Description");
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
        if (!voteCategory.equals(COMMENTVOTE) && !voteCategory.equals(POSTVOTE)) {
            throw new PortfolioException("Vote category is invalid!");
        }
    }

    private void validateMandatoryField(String field, int maxLength, String fieldName) {
        validateMandatoryField(field, maxLength, fieldName);
        if (field.length() > maxLength) {
            throw new PortfolioException(fieldName + " is too long (" + field.length() + "). Max length is - '" + maxLength + "'!");
        }
    }

    private void validateMandatoryField(String field, String fieldName) {
        if (field == null || isEmpty(field)) {
            throw new PortfolioException("'" + fieldName + "'" + " is empty or null. '" + fieldName + "' cannot be empty or null!");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !(email.contains("@") && email.contains("."))) {
            throw new PortfolioException("Email is invalid!");
        }
    }
}
