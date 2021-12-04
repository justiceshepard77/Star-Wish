package com.example.star_wish;

import android.media.Image;

public class Gift {
    String title;
    String cost;
    String url;
    String imageUrl;
    int image;

    Gift(String title, String cost, String url, String imageUrl,int image) {
        this.title = title;
        this.cost = cost;
        this.url = url;
        this.imageUrl = imageUrl;
        this.image = image;
    }
}
