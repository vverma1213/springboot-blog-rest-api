package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Comment comment =mapDtoToEntity(commentDto);

        //retrieve post by id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("POST","id",String.valueOf(postId)));

        //set post to comment entity
        comment.setPost(post);

        //save comment to db
        Comment newComment = commentRepository.save(comment);

        return mapEntityToDto(newComment);
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        return comments.stream().map(comment ->mapEntityToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getPostCommentByCommentId(Long postId, Long commentId) {

        //retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("POST","id",String.valueOf(postId)));

        //retreive comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",String.valueOf(commentId)));

        //comment should belong  to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"comment does not belong to post.");
        }

        return mapEntityToDto(comment);

    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        //retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("POST","id",String.valueOf(postId)));

        //retreive comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",String.valueOf(commentId)));

        //comment should belong  to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"comment does not belong to post.");
        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        return mapEntityToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //retrieve post by post id
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("POST","id",String.valueOf(postId)));

        //retreive comment by comment id
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("comment","id",String.valueOf(commentId)));

        //comment should belong  to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"comment does not belong to post.");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapEntityToDto(Comment comment){
        CommentDto commentDto = modelMapper.map(comment,CommentDto.class);
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setBody(comment.getBody());
//        commentDto.setEmail(comment.getEmail());
        return commentDto;
    }

    private Comment mapDtoToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
//        comment.setId(commentDto.getId());
//        comment.setBody(commentDto.getBody());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
        return comment;
    }
}
