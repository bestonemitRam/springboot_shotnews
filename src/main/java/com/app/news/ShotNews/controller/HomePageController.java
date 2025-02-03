package com.app.news.ShotNews.controller;


import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.entities.Category;
import com.app.news.ShotNews.entities.Post;
import com.app.news.ShotNews.repositories.CategoryRepository;
import com.app.news.ShotNews.repositories.PostRepository;
import com.app.news.ShotNews.response.ResponseApi;
import com.app.news.ShotNews.services.impl.HomePageService;
import com.app.news.ShotNews.services.impl.PostServiceImpl;
import com.app.news.ShotNews.services.serviceInter.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


@RestController
@RequestMapping("/api/home")
public class HomePageController
{

    @Autowired
    private HomePageService homePageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${project.image}")
    private String path;

    @Autowired
    FileService fileService;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private PostRepository postRepository;


    @GetMapping
    public ResponseEntity<ResponseApi> getHomePageData()
    {

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        Map<String, Object> response = new HashMap<>();

        List<Post> hotNews = homePageService.getHotNews();
        List<Post> sliderNews = homePageService.getSliderNews();
        List<Post> liveNews = homePageService.getLiveNews();
        List<Post> groundLevelNews = homePageService.getGroundLevelNews();
        List<Post> mostWatchedNews = homePageService.getMostWatchedNews();
        List<Category> categories = categoryRepository.findAll();
        Map<String, List<Post>> topPostsMap = new HashMap<>();
       for (Category category : categories)
        {
            Pageable topThree = PageRequest.of(0, 3);
             if(!category.getSlug().equalsIgnoreCase("home"))
             { List<Post> topPosts = postRepository.findTop3PostsByCategorySlug(category.getSlug(),topThree);
                topPostsMap.put(category.getName(),mapPosts(topPosts, baseUrl));
             }

        }





        response.put("hotNews", mapPosts(hotNews, baseUrl));
        response.put("sliderNews", mapPosts(sliderNews, baseUrl));
        response.put("liveNews", mapPosts(liveNews, baseUrl));
        response.put("groundLevelNews", mapPosts(groundLevelNews, baseUrl));
        response.put("mostWatchedNews", mapPosts(mostWatchedNews, baseUrl));
        response.put("category", topPostsMap);
      //  response.put("allCategory", mapPosts((List<Post>) topPostsMap, baseUrl));


       return new ResponseEntity<>(ResponseApi
                .builder()
                .status(true)
                .message(AppConstant.response)
                .data(response)
                .build(), HttpStatus.OK);


    }

    private List<Post> mapPosts(List<Post> posts, String baseUrl) {
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
    @GetMapping(value = "image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void download(@PathVariable("imageName") String imageName , HttpServletResponse response) throws IOException
    {
        InputStream resource=fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    @GetMapping(value = "video/{imageName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadVideo(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = fileService.getResource(path, imageName); // Adjust the path for videos
        response.setContentType("video/mp4"); // Set the content type to video/mp4
        response.setHeader("Content-Disposition", "inline; filename=\"" + imageName + "\""); // Inline so it plays in the browser
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
