package com.example.certif.dto;

import com.example.certif.entity.StudyComment;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor // 매개변수가 아예 없는 기본 생성자 자동 생성
@Getter
@Setter
@ToString

public class StudyCommentDto {
    private Long commentId; // 댓글 id
    private Long userId; // 댓글 작성자 -> 출력은 닉네임으로 바꿔야 함
    private Long postId; // 댓글의 부모 id - 게시판 글 id
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private String profileImage;

    public static StudyCommentDto createStudyCommentDto(StudyComment studyComment) {
        return new StudyCommentDto(
                studyComment.getId(),
                studyComment.getUser().getId(),
                studyComment.getPost().getId(),
                studyComment.getUser().getNickname(),
                studyComment.getContent(),
                studyComment.getCreatedAt(),
                studyComment.getUser().getProfileImage()
        );
    }
}
