package com.app.news.ShotNews.config;

public class SlugUtils {
    public static String toSlug(String input) {
        return input.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-$", "");
    }
}