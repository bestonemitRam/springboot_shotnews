package com.app.news.ShotNews.controller;

import com.app.news.ShotNews.config.AppConstant;
import com.app.news.ShotNews.config.SlugUtils;
import com.app.news.ShotNews.entities.Category;
import com.app.news.ShotNews.exceptions.APIException;
import com.app.news.ShotNews.repositories.CategoryRepository;
import com.app.news.ShotNews.response.ResponseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:54150")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category)
    {
        category.setSlug(SlugUtils.toSlug(category.getName()));

        if (categoryRepository.existsByName(category.getName())) {
            throw new APIException("Name '" + category.getName() + "' already exists. Please use a different name.");
        }


        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.CREATED);
    }

    @GetMapping("/getMenu")
    public ResponseEntity<ResponseApi> getALLMenu()
    {
        return  new ResponseEntity<>(ResponseApi
                .builder()
                .status(true)
                .message(AppConstant.response)
                .data( categoryRepository.findAll())
                .build(), HttpStatus.CREATED);
    }
}
