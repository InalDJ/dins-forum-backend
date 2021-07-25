package com.java.springportfolio.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String token;

    @Column
    private Instant createdDate;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, Instant createdDate) {
        this.id = id;
        this.token = token;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
