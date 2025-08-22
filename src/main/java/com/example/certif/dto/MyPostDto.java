package com.example.certif.dto;

import com.example.certif.entity.SharePost;
import com.example.certif.entity.StudyPost;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostDto {
    private Long id;
    private String title;
    private  String content;
    private String type; // "study" or "share"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // StudyPost 변환
    public static MyPostDto fromEntity(StudyPost post, String type) {
        return MyPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .type(type)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // SharePost 변환
    public static MyPostDto fromEntity(SharePost post, String type) {
        return MyPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .type(type)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
