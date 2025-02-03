package com.app.news.ShotNews.services.impl;


import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.config.SlugUtils;
import com.app.news.ShotNews.entities.Category;
import com.app.news.ShotNews.entities.Post;
import com.app.news.ShotNews.exceptions.APIException;
import com.app.news.ShotNews.repositories.CategoryRepository;
import com.app.news.ShotNews.repositories.PostRepository;
import com.app.news.ShotNews.response.PostResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Post createPost(Post post)
    {
        if (postRepository.existsBySlug(SlugUtils.toSlug(post.getTitle())))
        {
            throw new APIException("Title  '" + post.getTitle() + "' already published. Please use a different title.");
        }
        post.setSlug(SlugUtils.toSlug(post.getTitle()));

        return postRepository.save(post);
    }

 public PostResponse getPostBySlug(String slug)
 {

     String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

     Optional<Post> post = postRepository.findBySlug(slug);

        if (post.isPresent())
        {
            // Increment view count
            Post foundPost = post.get();

            foundPost.setViews(foundPost.getViews() + 1);

            postRepository.save(foundPost);

            List<Post> relatedPosts = postRepository.findByCategoryAndIdNot(
                    foundPost.getCategory(),
                    foundPost.getId()

            );

            foundPost.setImagePath(baseUrl + AppConstant.imageUrl +foundPost.getImagePath());
        return new PostResponse(foundPost, mapPosts(relatedPosts,baseUrl));
       }
        else
        {
            throw new EntityNotFoundException("Post not found");
        }
    }

    private List<Post> mapPosts(List<Post> posts, String baseUrl)
    {
        return posts.stream().map(post ->
        {
            Post mappedPost = new Post();
            mappedPost.setId(post.getId());
            mappedPost.setTitle(post.getTitle());
            mappedPost.setSlug(post.getSlug());
            mappedPost.setDescription(post.getDescription());
            mappedPost.setContent(post.getContent());
            mappedPost.setIsLive(post.getIsLive());
            mappedPost.setIsSlider(post.getIsSlider());
            mappedPost.setCategory(post.getCategory());
            mappedPost.setSubcategory(post.getSubcategory());
            mappedPost.setCreatedAt(post.getCreatedAt());
            mappedPost.setUpdatedAt(post.getUpdatedAt());
            mappedPost.setViews(post.getViews());
            mappedPost.setIsActive(post.getIsActive());
            mappedPost.setUrlType(post.getUrlType());
            mappedPost.setImagePath(baseUrl + AppConstant.imageUrl + post.getImagePath());
            return mappedPost;
        }).collect(Collectors.toList());
    }


    public List<Post> getMostViewedPosts()
    {
        return postRepository.findTop5ByOrderByViewsDesc();
    }

    public List<Post> getPostsByCategorySlug(String categorySlug, String subcategorySlug) {
        if (subcategorySlug != null && !subcategorySlug.isEmpty())
        {
            // Fetch posts by both category and subcategory slug
            return postRepository.findByCategory_SlugAndSubcategory_Slug(categorySlug, subcategorySlug);
        } else {
            // Fetch posts by category slug only
            return postRepository.findByCategory_Slug(categorySlug);
        }
    }

    public Map<Category, List<Post>> findTop4PostsForAllCategories() {
        // Fetch all categories
        List<Category> categories = categoryRepository.findAll();

        // Create a map to store top 4 posts for each category
        Map<Category, List<Post>> topPostsMap = new HashMap<>();

        // For each category, get the top 4 most viewed posts
        for (Category category : categories) {
//            List<Post> topPosts = postRepository.findTop4PostsByCategorySlug(category.getSlug());
//            topPostsMap.put(category, topPosts);
        }

        return topPostsMap;
    }
}
