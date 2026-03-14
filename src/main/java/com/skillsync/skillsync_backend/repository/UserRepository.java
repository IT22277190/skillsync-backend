package com.skillsync.skillsync_backend.repository;

import java.util.Optional;

import com.skillsync.skillsync_backend.entity.UserEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
    
}
