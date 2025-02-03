package com.app.news.ShotNews.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(unique = true)
    private String slug;

    private Boolean isHot;
    private Boolean isSlider;  // Nullable to allow optional setting
    private Boolean isLive;
    private Boolean isHandpicked;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = true)
    private Subcategory subcategory;


    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    private String content;

    private int views = 0;

    private String imagePath;

    @Column(nullable = false)
    private String urlType;

}
