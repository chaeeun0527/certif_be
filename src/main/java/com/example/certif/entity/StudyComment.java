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
        if(dto.getCommentId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        if(dto.getPostId() != studyPost.getId())
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다");

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
        // 예외 발생
        if(this.id != dto.getId())
            throw new IllegalArgumentException("댓글 수정 실패! 잘못된 id가 입력됐습니다.");
        // 객체의 갱신 - 댓글 본문 내용 갱신
        if(dto.getContent() != null)
            this.content = dto.getContent(); // this가 target 즉, 바뀜 당해야 하는 기존 내용

    }
}


