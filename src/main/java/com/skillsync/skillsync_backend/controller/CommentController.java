package com.skillsync.skillsync_backend.controller;

import com.skillsync.skillsync_backend.entity.CommentEntity;
import com.skillsync.skillsync_backend.entity.CommentPostRequestEntity;
import com.skillsync.skillsync_backend.entity.IdObjectEntity;
import com.skillsync.skillsync_backend.service.CommentService;
import com.skillsync.skillsync_backend.service.ResponseObjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/insertcomment")
    public ResponseEntity<ResponseObjectService> insertComment(@RequestBody CommentPostRequestEntity postedComment) {
        CommentEntity inputComment = postedComment.getCommentEntity();
        IdObjectEntity inputPostId = postedComment.getPostId();
        return new ResponseEntity<ResponseObjectService>(commentService.insertComment(inputComment, inputPostId.getId()), HttpStatus.OK);
    }

    @PostMapping("/getcomments") 
    public ResponseEntity<ResponseObjectService> getComments(@RequestBody IdObjectEntity inputPostId) {
        return new ResponseEntity<ResponseObjectService>(commentService.getComments(inputPostId.getId()), HttpStatus.OK);
    }
    
    @DeleteMapping("/deletecomment/{commentId}/{postId}")
    public ResponseEntity<ResponseObjectService> deleteComment(
        @PathVariable("commentId") String commentId,
        @PathVariable("postId") String postId
    ) {
        ResponseObjectService response = commentService.deleteComment(commentId, postId);
        HttpStatus status = "success".equals(response.getStatus()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/comment/{commentId}") // Correct endpoint
    public ResponseEntity<ResponseObjectService> getComment(@PathVariable String commentId) {
        ResponseObjectService response = commentService.getCommentById(commentId);
        HttpStatus status = "success".equals(response.getStatus()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }
    @PutMapping("/editcomment/{commentId}/{postId}")
    public ResponseEntity<ResponseObjectService> editComment(
        @PathVariable("commentId") String commentId,
        @PathVariable("postId") String postId,
        @RequestBody CommentEntity updatedComment
    ) {
        ResponseObjectService response = commentService.editComment(commentId, postId, updatedComment);

        if (response.isSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK); // Return success response
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // Return failure response
        }
    }

}