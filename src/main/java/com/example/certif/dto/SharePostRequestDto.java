package com.example.certif.dto;

import com.example.certif.entity.SharePostEntity;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SharePostRequestDto {
    private String title;
    private String content;
    private Long categoryId;

    // 엔티티로 변환하는 메서드. User와 Category 엔티티를 인자로 받습니다.
    public SharePostEntity toEntity(User user, Category category) {
        return SharePostEntity.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .category(category)
                .build();
    }

}
