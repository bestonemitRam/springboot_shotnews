package com.app.news.ShotNews.controller;


import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.payload.MenuDTO;
import com.app.news.ShotNews.response.ResponseApi;
import com.app.news.ShotNews.services.serviceInter.FileService;
import com.app.news.ShotNews.services.serviceInter.MenuService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class MainController
{

    @Autowired
    MenuService menuService;

    @Value("${project.image}")
    private String path;

    @Autowired
    FileService fileService;


    @PostMapping("createMenu")
    public ResponseEntity<ResponseApi> createMenu(@RequestBody List<MenuDTO> menuCategory)
    { return  new ResponseEntity<>(ResponseApi
               .builder()
               .status(true)
               .message(AppConstant.response)
               .data( menuService.createMenu(menuCategory))
               .build(), HttpStatus.CREATED);
  }

    @PostMapping("trendingTopic")
    public ResponseEntity<ResponseApi> createTrendingTopic(@RequestBody List<MenuDTO> menuCategory)
    { return  new ResponseEntity<>(ResponseApi
            .builder()
            .status(true)
            .message(AppConstant.response)
            .data( menuService.createTrending(menuCategory))
            .build(), HttpStatus.CREATED);
    }

    @PostMapping("creteSetting")
    public ResponseEntity<ResponseApi> createAppSetting(@RequestParam String aboutApp,  @RequestParam MultipartFile img )
    { return  new ResponseEntity<>(ResponseApi
            .builder()
            .status(true)
            .message(AppConstant.response)
            .data( menuService.createAbout(aboutApp,img))
            .build(), HttpStatus.CREATED);
    }


    @GetMapping("getMenu")
    public ResponseEntity<ResponseApi> getALLMenu()
    {
        return  new ResponseEntity<>(ResponseApi
            .builder()
            .status(true)
            .message(AppConstant.response)
            .data( menuService.getALLMenu())
            .build(), HttpStatus.CREATED);
    }

    @GetMapping("getSettings")
    public ResponseEntity<ResponseApi> getSetting()
    {

        return  new ResponseEntity<>(ResponseApi.builder()
                .status(true)
                .message(AppConstant.response)
                .data( menuService.getALLSetting())
                .build(), HttpStatus.CREATED);
    }

    @GetMapping(value = "image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void download(@PathVariable("imageName") String imageName , HttpServletResponse response) throws IOException
    {
        InputStream resource=fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
@GetMapping(value = "video/{imageName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
public void downloadVideo(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException
{
    InputStream resource = fileService.getResource(path, imageName); // Adjust the path for videos
    response.setContentType("video/mp4"); // Set the content type to video/mp4
    response.setHeader("Content-Disposition", "inline; filename=\"" + imageName + "\""); // Inline so it plays in the browser
    StreamUtils.copy(resource, response.getOutputStream());
}


}













