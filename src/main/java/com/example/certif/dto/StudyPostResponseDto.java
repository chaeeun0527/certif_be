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
public class StudyPostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    private String nickname;
    private Long userId;

    public static StudyPostResponseDto fromEntity(StudyPost studyPost) {
        StudyPostResponseDto dto = new StudyPostResponseDto();
        dto.setPostId(studyPost.getId());
        dto.setTitle(studyPost.getTitle());
        dto.setContent(studyPost.getContent());
        dto.setCategory(studyPost.getCategory().getName());
        dto.setCreatedAt(studyPost.getCreatedAt());
        dto.setNickname(studyPost.getUser().getNickname());
        dto.setUserId(studyPost.getUser().getId());
        return dto;
    }
}
