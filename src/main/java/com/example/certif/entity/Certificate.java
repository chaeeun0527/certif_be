package com.example.certif.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    // 카테고리 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Lob
    @Column
    private String overview;

    @Column(name = "official_site")
    private String officialSite;

    @Column
    private String organization;

    @Lob
    @Column(name = "exam_method")
    private String examMethod;

    @Lob
    @Column(name = "passing_criteria")
    private String passingCriteria;

    @Lob
    @Column
    private String qualification;

    @Column
    private String fee;

    @Lob
    @Column
    private String features;

}
