package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        return mapEntityToDTO(postRepository.save(mapDtoToEntity(postDto)));
    }

    //convert DTO into entity
    private Post mapDtoToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return post;
    }

    @Override
    public List<PostDto> getAllPosts(){
        return postRepository.findAll().stream().map(post ->mapEntityToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Get","id",String.valueOf(id)));
        return mapEntityToDTO(post);
    }

    @Override
    public PostDto updatePostById(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",String.valueOf(id)));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        return mapEntityToDTO(postRepository.save(post));
    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("DELETE","id",String.valueOf(id)));
        postRepository.delete(post);
    }

    //Convert entity to DTO
    private PostDto mapEntityToDTO(Post newPost) {
        PostDto postResponse = new PostDto();
        postResponse.setId(newPost.getId());
        postResponse.setTitle(newPost.getTitle());
        postResponse.setDescription(newPost.getDescription());
        postResponse.setContent(newPost.getContent());
        return postResponse;
    }
}
