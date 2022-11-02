package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PostResponse getAllPosts(int pageNo,int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //Pageable pageable = PageRequest.of(pageNo,pageSize);

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Post> post = postRepository.findAll(pageable);
        //convert page object to list

        List<Post> listOfPost = post.getContent();

        List<PostDto> content =  listOfPost.stream().map(p ->mapEntityToDTO(p)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(post.getNumber());
        postResponse.setPageSize(post.getSize());
        postResponse.setTotalPage(post.getTotalPages());
        postResponse.setTotalElements(post.getTotalElements());
        postResponse.setLast(post.isLast());

        return postResponse;
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
