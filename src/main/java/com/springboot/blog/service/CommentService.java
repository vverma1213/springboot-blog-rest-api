package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto);

    List<CommentDto> getAllCommentsByPostId(Long postId);

    CommentDto getPostCommentByCommentId(Long postId, Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);

    void deleteComment(Long postId, Long commentId);
}
