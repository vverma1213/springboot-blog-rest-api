package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") Long postId,
                                                    @Valid @RequestBody CommentDto commentDto){
        CommentDto commentResponse = commentService.createComment(postId,commentDto);

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getPostCommentByCommentId(@PathVariable(value="postId") Long postId,
                                                                @PathVariable(value="commentId") Long commentId){
        return ResponseEntity.ok(commentService.getPostCommentByCommentId(postId,commentId));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value="postId") Long postId,
                                                                @PathVariable(value="commentId") Long commentId,
                                                              @Valid @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateComment(postId,commentId,commentDto));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>(String.format("Comment %s is successfully delete for postid:%s",commentId,postId),HttpStatus.OK);
    }

}
