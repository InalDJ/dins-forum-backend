package com.java.springportfolio.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FileRecord> fileRecords;

    @Nullable
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    public void addFileRecord(FileRecord fileRecord) {
        if (fileRecord == null) {
            return;
        }
        if (fileRecords == null) {
            fileRecords = new ArrayList<>();
        }
        fileRecords.add(fileRecord);
        fileRecord.setPost(this);
    }

    public void addFileRecordList(List<FileRecord> fileRecordList) {
        if (fileRecordList == null || fileRecordList.isEmpty()) {
            return;
        }
        for (FileRecord fileRecord : fileRecordList) {
            addFileRecord(fileRecord);
        }
    }
}
