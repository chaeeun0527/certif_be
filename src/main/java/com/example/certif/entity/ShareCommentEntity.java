package com.example.certif.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "share_comment")
public class ShareCommentEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // 사용자 ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 게시물과 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private SharePostEntity sharePost;

    @Builder
    public ShareCommentEntity(String content, Long userId, SharePostEntity sharePost){
        this.content = content;
        this.userId = userId;
        this.sharePost = sharePost;
    }

    // 댓글 수정 메서드
    public void update(String content){
        if(content != null && !content.isEmpty()){
            this.content = content;
        }
    }
}
