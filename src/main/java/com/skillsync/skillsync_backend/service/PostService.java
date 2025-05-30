package com.skillsync.skillsync_backend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.skillsync.skillsync_backend.entity.DoubleIdObjectEntity;
import com.skillsync.skillsync_backend.entity.IdObjectEntity;
import com.skillsync.skillsync_backend.entity.PostByFollowing;
import com.skillsync.skillsync_backend.entity.PostEntity;
import com.skillsync.skillsync_backend.entity.UserEntity;
import com.skillsync.skillsync_backend.repository.PostRepository;
import com.skillsync.skillsync_backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserRepository userRepo;
    
    public ResponseObjectService insertPost(PostEntity inputPost) {
        ResponseObjectService responseObj = new ResponseObjectService();
        inputPost.setCreatedAt(Instant.now());
        responseObj.setStatus("success");
        responseObj.setMessage("success");
        responseObj.setPayload(postRepo.save(inputPost));
        return responseObj;
    }

    public ResponseObjectService deletePost(String postId) {                               //Delete Post
        ResponseObjectService response = new ResponseObjectService();

        Optional<PostEntity> optPost = postRepo.findById(postId);
        if (optPost.isPresent()) {
            postRepo.deleteById(postId);
            response.setStatus("success");
            response.setMessage("Post deleted successfully");
            response.setPayload(null);
        } else {
            response.setStatus("fail");
            response.setMessage("Post not found with ID: " + postId);
            response.setPayload(null);
        }

        return response;
    }

    public ResponseObjectService editPost(PostEntity inputPost) {                               //Edit Post
        ResponseObjectService response = new ResponseObjectService();

        // Find the post by its ID
        Optional<PostEntity> optPost = postRepo.findById(inputPost.getId());

        if (optPost.isPresent()) {
            // Get the existing post and update it
            PostEntity existingPost = optPost.get();

            existingPost.setContent(inputPost.getContent()); // Update content
            // Update other fields as needed, like `image`, `title`, etc.

            postRepo.save(existingPost); // Save the updated post

            response.setStatus("success");
            response.setMessage("Post updated successfully");
            response.setPayload(existingPost);
        } else {
            response.setStatus("fail");
            response.setMessage("Post not found");
            response.setPayload(null);
        }

        return response;
    }

    public ResponseObjectService findPostById(String postId) {
        ResponseObjectService response = new ResponseObjectService();

        Optional<PostEntity> post = postRepo.findById(postId); // Find post by ID

        if (post.isPresent()) {
            response.setStatus("success");
            response.setMessage("Post found");
            response.setPayload(post.get()); // Set the found post in the response
        } else {
            response.setStatus("fail");
            response.setMessage("Post not found");
            response.setPayload(null); // No post found
        }

        return response;
    }
    


    public ResponseObjectService findPostByUserId(IdObjectEntity inputUserId) {
        ResponseObjectService responseObj = new ResponseObjectService();
        Optional<List<PostEntity>> userPostsOpt = postRepo.findByUserIdOrderByCreatedAtDesc(inputUserId.getId());
        if (userPostsOpt.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find any post from user id: " + inputUserId.getId());
            responseObj.setPayload(null);
            return responseObj;
        } else {
            List<PostEntity> userPosts = userPostsOpt.get();
            responseObj.setStatus("success");
            responseObj.setMessage("success");
            responseObj.setPayload(userPosts);
            return responseObj;
        }
    }
    
    public ResponseObjectService findPostByFollowing(IdObjectEntity inputUserId) {
        ResponseObjectService responseObj = new ResponseObjectService();
        Optional<UserEntity> optUser = userRepo.findById(inputUserId.getId());
        if (optUser.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find any post from user id: " + inputUserId.getId());
            responseObj.setPayload(null);
            return responseObj;
        } else {
            UserEntity user = optUser.get();
            if (user.getFollowing() != null) {
                // if user followed someone, get their ids
                List<String> followingIds = new ArrayList<>();
                for (String id : user.getFollowing()) {
                    followingIds.add(id);
                }
                // based on these ids, get their equivalent posts
                List<PostByFollowing> listPosts = new ArrayList<>();
                for (String followingId : followingIds) {
                    // get following user info based on Id
                    UserEntity followingUser = new UserEntity();
                    Optional<UserEntity> optFollowingUser = userRepo.findById(followingId);
                    if (optFollowingUser.isPresent()) {
                        followingUser = optFollowingUser.get();
                    }

                    followingUser.setPassword("");
                    
                    // get equivalent posts
                    Optional<List<PostEntity>> followingPostsOpt = postRepo.findByUserId(followingId);
                    if (followingPostsOpt.isPresent()) {
                        // if followed account has any post, collect them
                        List<PostEntity> followingPosts = followingPostsOpt.get();
                        if (followingPosts != null) {
                            for (PostEntity item : followingPosts) {
                                listPosts.add(new PostByFollowing(followingUser, item));
                            }
                        }
                    }
                }
                Collections.sort(listPosts, (o1, o2) -> o2.getPost().getCreatedAt().compareTo(o1.getPost().getCreatedAt()));
                responseObj.setStatus("success");
                responseObj.setMessage("success");
                responseObj.setPayload(listPosts);
                return responseObj;
            } else {
                responseObj.setStatus("fail");
                responseObj.setMessage("user id: " + inputUserId.getId() + " has empty following list");
                responseObj.setPayload(null);
                return responseObj;
            }
        }
    }

    public ResponseObjectService updatePostByComment(PostEntity inputPost) {
        ResponseObjectService responseObj = new ResponseObjectService();
        Optional<PostEntity> optPost = postRepo.findById(inputPost.getId());
        if (optPost.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find post id: " + inputPost.getId());
            responseObj.setPayload(null);
            return responseObj;
        } else {
            // inputPost.setCreatedAt(Instant.now());
            postRepo.save(inputPost);
            responseObj.setStatus("success");
            responseObj.setMessage("post is updated successfully");
            responseObj.setPayload(inputPost);
            return responseObj;
        }
    }

    public ResponseObjectService updatePostByLove(DoubleIdObjectEntity doubleId) {
        // id 1 - post Id, id 2 - user who liked post
        ResponseObjectService responseObj = new ResponseObjectService();
        Optional<PostEntity> optPost = postRepo.findById(doubleId.getId1());
        if (optPost.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find post id: " + doubleId.getId1());
            responseObj.setPayload(null);
            return responseObj;
        } else {
            PostEntity targetPost = optPost.get();
            List<String> loveList = targetPost.getLove();
            if (loveList == null) {
                loveList = new ArrayList<>();
            }
            // love and unlove a post
            if (!loveList.contains(doubleId.getId2())) {
                loveList.add(doubleId.getId2());
            } else {
                loveList.remove(doubleId.getId2());
            }
            targetPost.setLove(loveList);
            postRepo.save(targetPost);
            responseObj.setStatus("success");
            responseObj.setMessage("update love to the target post id: " + targetPost.getId());
            responseObj.setPayload(targetPost);
            return responseObj;
        }
    }

    public ResponseObjectService updatePostByShare(DoubleIdObjectEntity doubleId) {
        // id 1 - post Id, id 2 - user who shared post
        ResponseObjectService responseObj = new ResponseObjectService();
        Optional<PostEntity> optPost = postRepo.findById(doubleId.getId1());
        if (optPost.isEmpty()) {
            responseObj.setStatus("fail");
            responseObj.setMessage("cannot find post id: " + doubleId.getId1());
            responseObj.setPayload(null);
            return responseObj;
        } else {
            PostEntity targetPost = optPost.get();
            List<String> shareList = targetPost.getShare();
            if (shareList == null) {
                shareList = new ArrayList<>();
            }
            // save id of user who shared the post then update post
            shareList.add(doubleId.getId2());
            targetPost.setShare(shareList);
            postRepo.save(targetPost);
            // update post list of user who shared the post
            targetPost.setUserId(doubleId.getId2());
            targetPost.setId(null);
            targetPost.setContent("Shared a post: " + targetPost.getContent());
            targetPost.setLove(new ArrayList<>());
            targetPost.setShare(new ArrayList<>());
            targetPost.setComment(new ArrayList<>());
            postRepo.save(targetPost);

            responseObj.setStatus("success");
            responseObj.setMessage("add a share to the target post id: " + targetPost.getId());
            responseObj.setPayload(targetPost);
            return responseObj;
        }
    }
}
