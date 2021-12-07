package com.example.star_wish;

import android.media.Image;

public class Gift {
    String title;
    String cost;
    String url;
    String imageUrl;
    String description;

    Gift(String title, String cost, String url, String imageUrl,String description) {
        this.title = title;
        this.cost = cost;
        this.url = url;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
