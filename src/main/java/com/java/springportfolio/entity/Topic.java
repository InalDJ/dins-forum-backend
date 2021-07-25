package com.java.springportfolio.entity;


import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotBlank(message = "Topic name is required")
    private String name;

    @Column
    @NotBlank(message = "Description is required")
    private String description;

    @Nullable
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @Column
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Topic() {
    }

    public Topic(Long id, @NotBlank(message = "Topic name is required") String name, @NotBlank(message = "Description is required") String description, List<Post> posts,
                 Instant createdDate, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.posts = posts;
        this.createdDate = createdDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    @Transient
    public Integer getNumberOfPosts(){
       if(getPosts().isEmpty()){
           return 0;
       }
        return getPosts().size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(getId(), topic.getId()) &&
                Objects.equals(getName(), topic.getName()) &&
                Objects.equals(getDescription(), topic.getDescription()) &&
                Objects.equals(getPosts(), topic.getPosts()) &&
                Objects.equals(getCreatedDate(), topic.getCreatedDate()) &&
                getUser().equals(topic.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getPosts(), getCreatedDate(), getUser());
    }
}
