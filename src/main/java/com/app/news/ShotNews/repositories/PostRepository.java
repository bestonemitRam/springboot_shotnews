package com.app.news.ShotNews.repositories;

import com.app.news.ShotNews.entities.Category;
import com.app.news.ShotNews.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>
{

    boolean existsBySlug(String slug);
    Optional<Post> findBySlug(String slug);

    List<Post> findByCategory_Slug(String slug);


    // Find Hot News
    List<Post> findByIsHotTrueOrderByCreatedAtDesc();

    // Find Slider News
    List<Post> findByIsSliderTrueOrderByCreatedAtDesc();

    // Find Live News
    List<Post> findByIsLiveTrueOrderByCreatedAtDesc();

    // Find Ground-Level News by subcategory slug (e.g., "ground-news")
    List<Post> findBySubcategorySlugOrderByCreatedAtDesc(String subcategorySlug);

    // Find Top 5 Most Watched News
    List<Post> findTop5ByOrderByViewsDesc();// For most viewed posts
    List<Post> findByCategory_SlugAndSubcategory_Slug(String categorySlug, String subcategorySlug);
    // Find posts by category excluding the current post
    List<Post> findByCategoryAndIdNot(Category category, Long currentPostId);
}