package com.springboot.blog.payload;

import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private String description;
    private String title;
    private String content;
}
