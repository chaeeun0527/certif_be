package com.example.certif.entity;

import com.example.certif.dto.StudyCommentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name ="study_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StudyComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계! -> 여러 댓글이 하나의 게시글에 속함
    @JoinColumn(name = "post_id", nullable = false) //db에서 외래키로 연결되는 컬럼 명시
    private StudyPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static StudyComment createComment(StudyCommentDto dto, StudyPost studyPost, User user) {
        return new StudyComment(
                null,               // id는 null로 둬서 DB에서 생성
                dto.getContent(),   // content
                null,               // createdAt 자동생성 -> null 전달
                null,               // updatedAt 자동생성 -> null 전달
                studyPost,          // 게시글 엔티티
                user                // 작성자 엔티티 (필수!)
        );
    }

    public void patch(StudyCommentDto dto) {
        if (dto.getContent() != null && !dto.getContent().isEmpty()) {
            this.content = dto.getContent();
        }
    }
}


