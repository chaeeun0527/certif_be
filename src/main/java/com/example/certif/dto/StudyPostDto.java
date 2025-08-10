package com.example.certif.dto;

import com.example.certif.entity.StudyPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class StudyPostDto {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    private Long user_id;

    public static StudyPostDto createStudyPostDto(StudyPost studyPost) {
        StudyPostDto dto = new StudyPostDto();
        dto.setPostId(studyPost.getId());
        dto.setTitle(studyPost.getTitle());
        dto.setContent(studyPost.getContent());
        dto.setCategory(studyPost.getCategory().getName());
        dto.setCreatedAt(studyPost.getCreatedAt());
        dto.setUser_id(studyPost.getUser().getId());
        return dto;
    }


}

