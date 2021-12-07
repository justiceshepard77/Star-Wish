package com.example.star_wish;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.star_wish.databinding.ActivityMainScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {

    String[] categories = {"Best Selling", "Best Friend", "For Family", "For Partner", "For Kids"};
    //private Gift[] gifts;
    ArrayList<Gift> gifts = new ArrayList<Gift>();

    private String selectedCategory = "Best Selling";
    private String oldCategory = "Best Selling";


    private ImageView bestSellingImageView;
    private ImageView BestFriendImageView;
    private ImageView FamilyImageView;
    private ImageView PartnerImageView;
    private ImageView kidsImageView;

    int bestSellingImage = R.drawable.bestselling;
    int BestFriendImage = R.drawable.him;
    int FamilyImage = R.drawable.dad;
    int PartnerImage = R.drawable.her;
    int kidsImage = R.drawable.kids;

    int bestSellingImage_selected = R.drawable.bestselling_selected;
    int BestFriendImage_selected = R.drawable.him_selected;
    int FamilyImage_selected = R.drawable.dad_selected;
    int PartnerImage_selected = R.drawable.her_selected;
    int kidsImage_selected = R.drawable.kids_selected;

    int CategoryIdx;

    ActivityMainScreenBinding binding;
    GridView simpleGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        bestSellingImageView = findViewById(R.id.bestSellingImageView);
        BestFriendImageView = findViewById(R.id.BestFriendImageView);
        FamilyImageView = findViewById(R.id.FamilyImageView);
        PartnerImageView = findViewById(R.id.PartnerImageView);
        kidsImageView = findViewById(R.id.kidsImageView);

        addButtons();

        simpleGrid = (GridView) findViewById(R.id.giftGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), gifts);
        simpleGrid.setAdapter(customAdapter);
        CategoryIdx = 1;
        loadGiftsForCategory("Best Selling");
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainScreen.this,"You clicked on " + gifts.get(i).title,Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gifts.get(i).url));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = this.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Last Category", CategoryIdx);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getSharedPreferences("application", Context.MODE_PRIVATE);
        CategoryIdx = sharedPref.getInt("Last Category", 1);
        switch (CategoryIdx) {
            case 1:
                selectedCategory = "Best Selling";
                deselectOldCategory(oldCategory,selectedCategory);
                bestSellingImageView.setImageResource(bestSellingImage_selected);
                break;
            case 2:
                selectedCategory = "Best Friend";
                deselectOldCategory(oldCategory,selectedCategory);
                BestFriendImageView.setImageResource(BestFriendImage_selected);
                break;
            case 3:
                selectedCategory = "For Family";
                deselectOldCategory(oldCategory,selectedCategory);
                FamilyImageView.setImageResource(FamilyImage_selected);
                break;
            case 4:
                selectedCategory = "For Partner";
                deselectOldCategory(oldCategory,selectedCategory);
                PartnerImageView.setImageResource(PartnerImage_selected);
                break;
            case 5:
                selectedCategory = "For Kids";
                deselectOldCategory(oldCategory,selectedCategory);
                kidsImageView.setImageResource(kidsImage_selected);
                break;
        }
        loadGiftsForCategory(selectedCategory);
    }

    public void loadGiftsForCategory (String category) {
        UpdateUI(category);
    }

    public void UpdateUI(String category){
        ArrayList<ArrayList<String>> Results = TopicWebScrape(category);
        ArrayList<String> ImageUrls = Results.get(0);
        ArrayList<String> Urls = Results.get(1);
        ArrayList<String> Titles = Results.get(2);
        ArrayList<String> Prices = Results.get(3);
        ArrayList<String> Descriptions = Results.get(4);

        gifts.clear();
        for (int i = 0; i < ImageUrls.size(); i++){
            Gift gift = new Gift(Titles.get(i),Prices.get(i), Urls.get(i), ImageUrls.get(i), Descriptions.get(i));
            gifts.add(gift);
        }

        simpleGrid.invalidateViews();
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), gifts);
        simpleGrid.setAdapter(customAdapter);
    }

    public void deselectOldCategory(String oldCat, String newCat) {
        switch (oldCat) {
            case "Best Selling":
                bestSellingImageView.setImageResource(bestSellingImage);
                break;
            case "Best Friend":
                BestFriendImageView.setImageResource(BestFriendImage);
                break;
            case "For Family":
                FamilyImageView.setImageResource(FamilyImage);
                break;
            case "For Partner":
                PartnerImageView.setImageResource(PartnerImage);
                break;
            case "For Kids":
                kidsImageView.setImageResource(kidsImage);
                break;
        }
        oldCategory = newCat;
    }

    public void addButtons() {
        LinearLayout bestSellingButton = (LinearLayout )findViewById(R.id.bestSellingButton);
        LinearLayout BestFriendButton = (LinearLayout )findViewById(R.id.forBestFriendButton);
        LinearLayout FamilyButton = (LinearLayout )findViewById(R.id.forFamilyButton);
        LinearLayout PartnerButton = (LinearLayout )findViewById(R.id.PartnerButton);
        LinearLayout kidsButton = (LinearLayout )findViewById(R.id.kidsButton);

        bestSellingImageView.setImageResource(bestSellingImage_selected);

        bestSellingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CHANGE BUTTON UI
                Log.v("Star Wish", "Pressed bestSellingButton!");
                selectedCategory = "Best Selling";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                bestSellingImageView.setImageResource(bestSellingImage_selected);

                // GET GIFTS
                CategoryIdx = 1;
                loadGiftsForCategory("Best Selling");
            }
        });

        BestFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed Best Friend Button!");
                selectedCategory = "Best Friend";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                BestFriendImageView.setImageResource(BestFriendImage_selected);

                CategoryIdx = 2;
                loadGiftsForCategory("Best Friend");
            }
        });
        FamilyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed For Family Button!");
                selectedCategory = "For Family";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                FamilyImageView.setImageResource(FamilyImage_selected);

                CategoryIdx = 3;
                loadGiftsForCategory("For Family");
            }
        });
        PartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed For Partner Button!");
                selectedCategory = "For Partner";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                PartnerImageView.setImageResource(PartnerImage_selected);

                CategoryIdx = 4;
                loadGiftsForCategory("For Partner");
            }
        });
        kidsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed For Kids Button!");
                selectedCategory = "For Kids";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                kidsImageView.setImageResource(kidsImage_selected);

                CategoryIdx = 5;
                loadGiftsForCategory("For Kids");

            }
        });
    }
    public void showAlert(View view) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        // add the buttons
        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logout_user(view);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void logout_user(View view) {
        FirebaseAuth.getInstance().signOut();
        //startActivity(new Intent(this, FirstActivity.class));
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show();
        finish();
    }

    private class getImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public getImageFromURL(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public ArrayList<ArrayList<String>> TopicWebScrape(String topic) {
        ArrayList<String> ImgList = new ArrayList<String>();
        ArrayList<String> UrlList = new ArrayList<String>();
        ArrayList<String> TitleList = new ArrayList<String>();
        ArrayList<String> PriceList = new ArrayList<String>();
        ArrayList<String> DescriptionList = new ArrayList<String>();
        ArrayList<ArrayList<String>> Results = new ArrayList<ArrayList<String>>();
        String url = new String();

        if (topic == "Best Selling"){
            url = "https://www.goodhousekeeping.com/holidays/gift-ideas/g38170847/most-popular-gifts-2021/";
        } else if (topic == "Best Friend"){
            url = "https://www.goodhousekeeping.com/holidays/gift-ideas/g4670/best-friend-gifts/";
        } else if (topic == "For Family"){
            url = "https://www.goodhousekeeping.com/holidays/gift-ideas/g29263705/best-family-gifts/";
        } else if (topic == "For Partner"){
            url = "https://www.goodhousekeeping.com/holidays/gift-ideas/g4517/gifts-for-boyfriend/";
        } else if (topic == "For Kids"){
            url = "https://www.goodhousekeeping.com/holidays/gift-ideas/g203/gifts-for-kids/";
        }

        try {
            org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .get();

            Elements allinfo = (Elements) doc.getElementsByClass("listicle-slide listicle-slide-square listicle-slide-product ");

            for (Element element : allinfo){
                try {
                    if (ImgList.size() <= 6) {
                        String prodNum = element.getElementsByClass("listicle-slide-hed-number").text();
                        int myNum = Integer.parseInt(prodNum);
                        int limit = 6;
                        // Build Image Url List
                        try {
                            if (myNum <= limit) {
                                // Build Image Url List
                                String img = element.getElementsByTag("source").attr("data-srcset");
                                ImgList.add(img);

                                // Build Amazon Url List
                                String urltemp = element.getElementsByClass("product-btn-link").attr("href");
                                UrlList.add(urltemp);

                                //Build Title List
                                String Title = element.getElementsByClass("listicle-slide-hed-text").text();
                                TitleList.add(Title);

                                //Build Price List
                                String Price = element.getElementsByClass("product-slide-price").text();
                                if (Price.length() > 10){
                                    String[] PriceArr = Price.split(" ");
                                    Price = PriceArr[1];
                                }
                                PriceList.add(Price);

                                //Add Product Descriptions
                                String Description = element.getElementsByClass("slideshow-slide-dek").text();
                                DescriptionList.add(Description);
                            }
                        } catch (Exception e) { }
                    } else {
                        break;
                    }
                } catch (Exception e) {}
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (HttpStatusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Results.add(ImgList);
        Results.add(UrlList);
        Results.add(TitleList);
        Results.add(PriceList);
        Results.add(DescriptionList);
        return Results;
    }
}


