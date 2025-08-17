package com.example.certif.dto;

//게시물 응답 DTO

import com.example.certif.entity.SharePostEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.LifecycleState;
import org.springframework.jca.support.LocalConnectionFactoryBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SharePostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ShareCommentResponseDto> comments;

    // Entity -> DTO 변환
    public SharePostResponseDto(SharePostEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.nickname = entity.getUser().getNickname();
        this.categoryId = entity.getCategory().getId();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.comments = entity.getComments().stream()
                .map(ShareCommentResponseDto::new)
                .collect(Collectors.toList());

    }
}
