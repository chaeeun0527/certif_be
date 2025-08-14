package com.example.certif.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "share_post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class SharePostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    // 사용자 ID -> 외래키
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 카테고리 ID -> 외래키
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;



    @OneToMany(mappedBy = "sharePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareCommentEntity> comments = new ArrayList<>();

    @Builder
    public SharePostEntity(String title, String content, com.example.certif.entity.User user, com.example.certif.entity.Category category){
        this.title = title;
        this.content = content;
        this.user = user;
        this.category = category;
    }

    // 게시물 수정 메서드
    public void update(String title, String content, Long categoryId){
        if(title != null && !title.isEmpty()){
            this.title = title;
        }
        if(content != null && !content.isEmpty()){
            this.content = content;
        }
        if(categoryId != null){
            this.category = category;
        }
    }

}
