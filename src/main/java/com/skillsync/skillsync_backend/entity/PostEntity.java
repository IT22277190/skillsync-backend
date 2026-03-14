package com.skillsync.skillsync_backend.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "post")
public class PostEntity {
    @Id
    private String id;

    private String userId;

    private String originalUserId;

    private String content;

    private String image; // New field for image URL or path

    private String video; // New field for video URL or path

    private Instant createdAt;

    List<String> love = new ArrayList<>();

    List<String> share = new ArrayList<>();

    List<CommentEntity> comment = new ArrayList<>();
}
