package com.skillsync.skillsync_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPostRequestEntity {
    private CommentEntity commentEntity;
    private IdObjectEntity postId;
}
