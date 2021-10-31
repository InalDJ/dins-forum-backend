package com.java.springportfolio.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column
    private String url;

    @Column
    private String path;

    @Column
    private Long userId;

    @Column
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Comment comment;
}
