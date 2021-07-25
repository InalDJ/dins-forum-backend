package com.java.springportfolio.entity;


import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long postId;

    @Column
    @NotBlank(message = "Post name cannot be emty or null")
    private String postName;

    @Column
    @Nullable
    @Lob
    private String description;

    @Column
    private Integer voteCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private User user;

    @Column
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Topic topic;

    @Nullable
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Nullable
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    public Post() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Nullable
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(@Nullable List<Comment> comments) {
        this.comments = comments;
    }

    @Nullable
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(@Nullable List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getPostId(), post.getPostId()) &&
                Objects.equals(getPostName(), post.getPostName()) &&
                Objects.equals(getDescription(), post.getDescription()) &&
                Objects.equals(getVoteCount(), post.getVoteCount()) &&
                getUser().equals(post.getUser()) &&
                Objects.equals(getCreatedDate(), post.getCreatedDate()) &&
                getTopic().equals(post.getTopic()) &&
                Objects.equals(getComments(), post.getComments()) &&
                Objects.equals(getVotes(), post.getVotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getPostName(), getDescription(), getVoteCount(), getUser(), getCreatedDate(), getTopic(), getComments(), getVotes());
    }
}
