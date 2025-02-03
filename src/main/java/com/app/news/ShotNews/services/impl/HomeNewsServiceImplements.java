package com.app.news.ShotNews.services.impl;

import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.entities.GroundNews;
import com.app.news.ShotNews.entities.HomeSlider;
import com.app.news.ShotNews.entities.HotNews;
import com.app.news.ShotNews.entities.LiveNews;
import com.app.news.ShotNews.payload.HomeDTO;
import com.app.news.ShotNews.repositories.GroundnewsRepo;
import com.app.news.ShotNews.repositories.HomeSliderRepo;
import com.app.news.ShotNews.repositories.HotNewsRepo;
import com.app.news.ShotNews.repositories.LiveNewsRepo;
import com.app.news.ShotNews.services.serviceInter.FileService;
import com.app.news.ShotNews.services.serviceInter.HomeNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class HomeNewsServiceImplements implements HomeNewsService
{

    @Autowired
    HotNewsRepo hotNewsRepo;

    @Autowired
    FileService fileService;


    @Autowired
    HomeSliderRepo homeSliderRepo;

    @Autowired
    LiveNewsRepo liveNewsRepo;

    @Autowired
    GroundnewsRepo groundnewsRepo;

    @Value("${project.image}")
    private String path;


    @Override
    public Boolean createdHotNews(String title, String description,  MultipartFile img)
    {

        String fileName = null;
        try {
            fileName = fileService.uploadImage(path, img);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        HotNews hotNews=HotNews.builder().title(title).description(description).slug("hotnews").image(fileName).build();

        HotNews hotNews1= hotNewsRepo.save(hotNews);

        if (hotNews1!=null)
        {
            return true;
        }
        else {
            return false;
        }


    }

    @Override
    public Boolean createHomeSlider(String title, String description,  MultipartFile img) {
        String fileName = null;
        try {
            fileName = fileService.uploadImage(path, img);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        HomeSlider homeSlider=HomeSlider.builder().title(title).description(description).slug("home_slider").image(fileName).build();

        HomeSlider homeSlider1=    homeSliderRepo.save(homeSlider);


        if (homeSlider1!=null)
        {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public Boolean createLiveNews(String title, String description, String urlType, MultipartFile url) {


        String fileName = null;
        try {
            fileName = fileService.uploadImage(path, url);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        LiveNews homeSlider=LiveNews.builder().title(title).description(description).slug("live_news").url(fileName).urlType(urlType).build();

        LiveNews homeSlider1=    liveNewsRepo.save(homeSlider);


        if (homeSlider1!=null)
        {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public Boolean createGroundLevel(String title, String description, String originType, MultipartFile url) {




        String fileName = null;
        try {
            fileName = fileService.uploadImage(path, url);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        GroundNews groundNews=GroundNews.builder().title(title).description(description).slug("GROUND_NEWS").originType(originType).image(fileName).build();

        GroundNews GroundNews1=    groundnewsRepo.save(groundNews);

    if (GroundNews1!=null)
        {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public HomeDTO getHomePageData()
     {

         String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

         HomeDTO homeDTO=new HomeDTO();

         List<HotNews> hotNewsList=hotNewsRepo.findAll();

         hotNewsList=  hotNewsList.stream()
                 .map(news ->
                 {
                     HotNews dto = new HotNews();
                     dto.setImage( baseUrl+ AppConstant.imageUrl+news.getImage());
                     dto.setDescription(news.getDescription());
                     dto.setSlug(news.getSlug());
                     dto.setTitle(news.getTitle());
                     dto.setCreatedAt(news.getCreatedAt());
                     dto.setUpdatedAt(news.getUpdatedAt());
                     dto.setIsActive(news.getIsActive());
                     dto.setId(news.getId());
                  return dto;
                 })
                 .collect(Collectors.toList());



         List<HomeSlider> homeSliders=homeSliderRepo.findAll();


         homeSliders=  homeSliders.stream()
                 .map(news ->
                 {
                     HomeSlider dto = new HomeSlider();

                     dto.setImage( baseUrl+ AppConstant.imageUrl+news.getImage());
                     dto.setDescription(news.getDescription());
                     dto.setSlug(news.getSlug());
                     dto.setTitle(news.getTitle());
                     dto.setCreatedAt(news.getCreatedAt());
                     dto.setUpdatedAt(news.getUpdatedAt());
                     dto.setIsActive(news.getIsActive());
                     dto.setId(news.getId());



                     return dto;
                 })
                 .collect(Collectors.toList());


         List<LiveNews> liveNewsList=liveNewsRepo.findAll();

         liveNewsList=  liveNewsList.stream()
                 .map(news ->
                 {
                     LiveNews dto = new LiveNews();

                     dto.setUrl( baseUrl+ AppConstant.imageUrl+news.getUrl());
                     dto.setDescription(news.getDescription());
                     dto.setSlug(news.getSlug());
                     dto.setTitle(news.getTitle());
                     dto.setUrlType(news.getUrlType());
                     dto.setId(news.getId());




                     return dto;
                 })
                 .collect(Collectors.toList());



         List<GroundNews> groundNewsList=groundnewsRepo.findAll();

         groundNewsList=  groundNewsList.stream()
                 .map(news -> {

                     GroundNews dto = new GroundNews();

                     dto.setImage( baseUrl+ AppConstant.imageUrl+news.getImage());
                     dto.setDescription(news.getDescription());
                     dto.setSlug(news.getSlug());
                     dto.setTitle(news.getTitle());
                     dto.setOriginType(news.getOriginType());

                     dto.setId(news.getId());



                     return dto;
                 })
                 .collect(Collectors.toList());




         homeDTO.setHomeSliders(homeSliders);
         homeDTO.setLiveNews(liveNewsList);
         homeDTO.setHotNews(hotNewsList);
         homeDTO.setGroundNewsList(groundNewsList);


        return homeDTO;
     }





}
