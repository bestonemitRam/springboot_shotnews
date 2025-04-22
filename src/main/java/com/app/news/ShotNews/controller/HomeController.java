package com.app.news.ShotNews.controller;


import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.entities.HotNews;
import com.app.news.ShotNews.response.ResponseApi;
import com.app.news.ShotNews.services.serviceInter.HomeNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {


    @Autowired
    HomeNewsService homeNewsService;
 @PostMapping("/createHotNews")
    public ResponseEntity<ResponseApi> createHotNews(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile img) {
       return new ResponseEntity<>(ResponseApi
                .builder()
                .status(homeNewsService.createdHotNews(title, description, img))
                .message(AppConstant.response)
                .build(), HttpStatus.CREATED);


    }
@PostMapping("/createHomeSlider")
    public ResponseEntity<ResponseApi> createHomeSlider(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile img) {
         return new ResponseEntity<>(ResponseApi
                .builder()
                .status(homeNewsService.createHomeSlider(title, description, img))
                .message(AppConstant.response)
                .build(), HttpStatus.CREATED);
 }
 @PostMapping("/liveNews")
 public ResponseEntity<ResponseApi> createLiveNews(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile url, @RequestParam String urlType) {
        return new ResponseEntity<>(ResponseApi
                .builder()
                .status(homeNewsService.createLiveNews(title, description, urlType, url))
                .message(AppConstant.response)
                .build(), HttpStatus.CREATED);
 }
 @PostMapping("/groundNews")
 public ResponseEntity<ResponseApi> createGroundNews(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile url,@RequestParam String originType)
    {
        return new ResponseEntity<>(ResponseApi
                .builder()
                
                .status(homeNewsService.createGroundLevel(title, description, originType,url))
                .message(AppConstant.response)
                .build(), HttpStatus.CREATED);
 }

 @GetMapping()
 public ResponseEntity<ResponseApi> getHomePagedata() {
        return new ResponseEntity<>(ResponseApi
                .builder()
                .status(true)
                .message(AppConstant.response)
                .data(homeNewsService.getHomePageData())
                .build(), HttpStatus.OK);
 }

}
