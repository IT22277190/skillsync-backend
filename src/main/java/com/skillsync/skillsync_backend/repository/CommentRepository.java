package com.skillsync.skillsync_backend.repository;

import com.skillsync.skillsync_backend.entity.CommentEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<CommentEntity, String> {
    
}
