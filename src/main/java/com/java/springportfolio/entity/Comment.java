package com.java.springportfolio.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotEmpty
    private String text;

    @Column
    private Integer voteCount = 0;

    @Nullable
    @Column
    private Long parentCommentId;

    @NotNull
    @Column
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Nullable
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentVote> commentVotes;

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Nullable
    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(@Nullable Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Nullable
    public List<CommentVote> getCommentVotes() {
        return commentVotes;
    }

    public void setCommentVotes(@Nullable List<CommentVote> commentVotes) {
        this.commentVotes = commentVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) &&
                Objects.equals(getText(), comment.getText()) &&
                Objects.equals(getVoteCount(), comment.getVoteCount()) &&
                Objects.equals(getParentCommentId(), comment.getParentCommentId()) &&
                Objects.equals(getCreatedDate(), comment.getCreatedDate()) &&
                getUser().equals(comment.getUser()) &&
                getPost().equals(comment.getPost()) &&
                Objects.equals(getCommentVotes(), comment.getCommentVotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getVoteCount(), getParentCommentId(), getCreatedDate(), getUser(), getPost(), getCommentVotes());
    }
}
