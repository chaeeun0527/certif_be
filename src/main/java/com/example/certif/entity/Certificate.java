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

    @Column(length = 500)
    private String overview;

    @Column(name = "official_site", length = 500)
    private String officialSite;

    @Column
    private String organization;

    @Column
    private String fee;

    @Column(name = "exam_method", length = 500)
    private String examMethod;

    @Column(name = "passing_criteria", length = 500)
    private String passingCriteria;

    @Column(length = 1000)
    private String qualification;

    @Column(length = 1000)
    private String features;

}
