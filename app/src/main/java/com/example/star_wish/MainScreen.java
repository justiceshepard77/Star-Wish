package com.example.star_wish;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.gargoylesoftware.htmlunit.*;
//import com.gargoylesoftware.htmlunit.html.*;
//import java.io.IOException;
//import java.util.List;

import com.bumptech.glide.Glide;
import com.example.star_wish.databinding.ActivityMainScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    String[] categories = {"Best Selling", "For Mom", "For Dad", "Electronics", "Home"};
    //private Gift[] gifts;
    ArrayList<Gift> gifts = new ArrayList<Gift>();

    private String selectedCategory = "Best Selling";
    private String oldCategory = "Best Selling";


    private ImageView bestSellingImageView;
    private ImageView momImageView;
    private ImageView dadImageView;
    private ImageView electronicsImageView;
    private ImageView homeImageView;

    int bestSellingImage = R.drawable.bestselling;
    int forMomImage = R.drawable.mom;
    int forDadImage = R.drawable.dad;
    int electronicsImage = R.drawable.electronics;
    int homeImage = R.drawable.home;

    int bestSellingImage_selected = R.drawable.bestselling_selected;
    int forMomImage_selected = R.drawable.mom_selected;
    int forDadImage_selected = R.drawable.dad_selected;
    int electronicsImage_selected = R.drawable.electronics_selected;
    int homeImage_selected = R.drawable.home_selected;

    ActivityMainScreenBinding binding;
    GridView simpleGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        bestSellingImageView = findViewById(R.id.bestSellingImageView);
         momImageView = findViewById(R.id.momImageView);
         dadImageView = findViewById(R.id.dadImageView);
         electronicsImageView = findViewById(R.id.electronicsImageView);
         homeImageView = findViewById(R.id.homeImageView);

        addButtons();
        addTempGifts();


//        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot()); // this line makes the giftCategoryScrollView break
//        int[] tempImages = {R.drawable.dad,R.drawable.mom,R.drawable.her};
//        GridAdapter gridAdapter = new GridAdapter(MainScreen.this,gifts,tempImages);
//        binding.giftGridView.setAdapter(gridAdapter);
//
//        binding.giftGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainScreen.this,"You clicked on " + gifts.get(i).title,Toast.LENGTH_SHORT).show();
//            }
//        });

        simpleGrid = (GridView) findViewById(R.id.giftGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), gifts);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainScreen.this,"You clicked on " + gifts.get(i).title,Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void austinWebScrape() {
        ArrayList<String> ImgList = new ArrayList<String>();
        ArrayList<String> TextList = new ArrayList<String>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    org.jsoup.nodes.Document doc = (org.jsoup.nodes.Document) Jsoup.connect("https://buybuggle.com/pages/21-gadgets/")
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                            .referrer("http://www.google.com")
                            .timeout(1000*5)
                            .get();
                    Elements allinfo = (Elements) doc.getElementsByTag("img");
                    //System.out.println(allinfo);
                    for (Element element : allinfo){
                        try {
                            String imgs = element.attr("src");
                            String[] imgs2 = imgs.split("\n");
                            for (int j = 0; j < imgs2.length; j++){
                                String string = "https://buybuggle.com/pages/21-gadgets/";
                                string = string + imgs2[j];
                                ImgList.add(string);
                            }
                        } catch(Exception e){ }
                    }
                    System.out.println(ImgList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new MainScreen.getImageFromURL((ImageView) bestSellingImageView).execute(ImgList.get(1));
                            //Glide.with(bestSellingImageView).load(ImgList.get(1)).into(bestSellingImageView);
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (HttpStatusException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void loadGiftsForCategory (String category) {
        switch (category){
            case "Best Selling":
            //
                Log.v("wishful","Loading gifts for category: " + category);

                OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.webscrapingapi.com/v1?api_key=eckAypfkdvQTF4tY8NZeHAyqoyZfuZIM&url=https://www.target.com/p/xbox-series-x-console/-/A-80790841")
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if (response.isSuccessful()) {
                        String myResponse = response.body().string();
                        Log.v("Wishful","My Response = " + myResponse);
                        MainScreen.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update the UI here

                            }
                        });
                    } else {
                    }
                    Log.v("Wishful","Response === " + response.body().string());

                }
            });
            //Response response = client.newCall(request).execute();

            //Log.v("Star Wish",response.toString());

            case "For Mom":

//                WebClient webClient = new WebClient(BrowserVersion.CHROME);
//
//                try {
//                    HtmlPage page = webClient.getPage("https://www.amazon.com/s?k=best+selling&ref=nb_sb_noss_1");
//
//                    webClient.getCurrentWindow().getJobManager().removeAllJobs();
//                    webClient.close();
//
////                    webClient.getOptions().setCssEnabled(false);
////                    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
////                    webClient.getOptions().setThrowExceptionOnScriptError(false);
////                    webClient.getOptions().setPrintContentOnFailingStatusCode(false);
//                    String title = page.getTitleText();
//                    Log.v("OutputHTML","Title Text: " + title);
//
//                    List<HtmlAnchor> links = page.getAnchors();
//                    for (HtmlAnchor link : links) {
//                        String href = link.getHrefAttribute();
//                        Log.v("OutputHTML","Link: " + href);
//                    }
//
//                } catch (IOException e) {
//                    System.out.println("An error occurred: " + e);
//                }
            case "For Dad":
                    austinWebScrape();
            case "Electronics":

            case "Home":

            default:
                // idk why it would be here

        }
    }
    public ArrayList<Gift> fetchData() {
        return new ArrayList<>();

    }
    public void addTempGifts() {
        Gift gift1 = new Gift("Apple Airpods","$129.99",
                "https://www.amazon.com/Apple-MME73AM-A-AirPods-3rd-Generation/dp/B09JQL3NWT/ref=asc_df_B09JQL3NWT/?tag=hyprod-20&linkCode=df0&hvadid=533377612228&hvpos=&hvnetw=g&hvrand=1855418286339174346&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9031645&hvtargid=pla-1479450628074&psc=1",
                "https://m.media-amazon.com/images/I/61ZRU9gnbxL._AC_SL1500_.jpg",
                electronicsImage);
        Gift gift2 = new Gift("AA Batteries","$29.99",
                "https://www.amazon.com/AmazonBasics-AA-Performance-Alkaline-Batteries/dp/B07NVTGRVZ/ref=zg-bs_hpc_1/135-2231544-6479548?pd_rd_w=zoOcQ&pf_rd_p=1e7b1982-fb44-47aa-b1ce-d356a8609d66&pf_rd_r=266D6GYACXA1W95B6XRD&pd_rd_r=2cecdc83-62d9-4e5e-9039-336b0405f419&pd_rd_wg=QNsfN&pd_rd_i=B00QWO9P0O&th=1",
                "https://images-na.ssl-images-amazon.com/images/I/51netU-Kn6L.__AC_SX300_SY300_QL70_FMwebp_.jpg",
                bestSellingImage);
        Gift gift3 = new Gift("Fire TV Stick","$19.99",
                "https://www.amazon.com/fire-tv-stick-with-3rd-gen-alexa-voice-remote/dp/B08C1W5N87/ref=zg-bs_electronics_1/135-2231544-6479548?pd_rd_w=iL6oB&pf_rd_p=1e7b1982-fb44-47aa-b1ce-d356a8609d66&pf_rd_r=266D6GYACXA1W95B6XRD&pd_rd_r=2cecdc83-62d9-4e5e-9039-336b0405f419&pd_rd_wg=QNsfN&pd_rd_i=B08C1W5N87&psc=1",
                "https://m.media-amazon.com/images/I/61+T2xNzR7S._AC_SL1000_.jpg", homeImage );

        Gift gift4 = new Gift("Fire TV Stick","$19.99",
                "https://www.amazon.com/fire-tv-stick-with-3rd-gen-alexa-voice-remote/dp/B08C1W5N87/ref=zg-bs_electronics_1/135-2231544-6479548?pd_rd_w=iL6oB&pf_rd_p=1e7b1982-fb44-47aa-b1ce-d356a8609d66&pf_rd_r=266D6GYACXA1W95B6XRD&pd_rd_r=2cecdc83-62d9-4e5e-9039-336b0405f419&pd_rd_wg=QNsfN&pd_rd_i=B08C1W5N87&psc=1",
                "https://m.media-amazon.com/images/I/61+T2xNzR7S._AC_SL1000_.jpg", homeImage );

        Gift gift5 = new Gift("Fire TV Stick","$19.99",
                "https://www.amazon.com/fire-tv-stick-with-3rd-gen-alexa-voice-remote/dp/B08C1W5N87/ref=zg-bs_electronics_1/135-2231544-6479548?pd_rd_w=iL6oB&pf_rd_p=1e7b1982-fb44-47aa-b1ce-d356a8609d66&pf_rd_r=266D6GYACXA1W95B6XRD&pd_rd_r=2cecdc83-62d9-4e5e-9039-336b0405f419&pd_rd_wg=QNsfN&pd_rd_i=B08C1W5N87&psc=1",
                "https://m.media-amazon.com/images/I/61+T2xNzR7S._AC_SL1000_.jpg", homeImage );
        Gift gift6 = new Gift("Apple Airpods","$129.99",
                "https://www.amazon.com/Apple-MME73AM-A-AirPods-3rd-Generation/dp/B09JQL3NWT/ref=asc_df_B09JQL3NWT/?tag=hyprod-20&linkCode=df0&hvadid=533377612228&hvpos=&hvnetw=g&hvrand=1855418286339174346&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9031645&hvtargid=pla-1479450628074&psc=1",
                "https://m.media-amazon.com/images/I/61ZRU9gnbxL._AC_SL1500_.jpg",
                electronicsImage);
        Gift gift7 = new Gift("AA Batteries","$29.99",
                "https://www.amazon.com/AmazonBasics-AA-Performance-Alkaline-Batteries/dp/B07NVTGRVZ/ref=zg-bs_hpc_1/135-2231544-6479548?pd_rd_w=zoOcQ&pf_rd_p=1e7b1982-fb44-47aa-b1ce-d356a8609d66&pf_rd_r=266D6GYACXA1W95B6XRD&pd_rd_r=2cecdc83-62d9-4e5e-9039-336b0405f419&pd_rd_wg=QNsfN&pd_rd_i=B00QWO9P0O&th=1",
                "https://images-na.ssl-images-amazon.com/images/I/51netU-Kn6L.__AC_SX300_SY300_QL70_FMwebp_.jpg",
                bestSellingImage);


        gifts.add(gift1);
        gifts.add(gift2);
        gifts.add(gift3);
        gifts.add(gift4);
        gifts.add(gift5);
        gifts.add(gift6);
        gifts.add(gift7);



    }

    public void deselectOldCategory(String oldCat, String newCat) {

        switch (oldCat) {
            case "Best Selling":
                bestSellingImageView.setImageResource(bestSellingImage);
            case "For Mom":
                momImageView.setImageResource(forMomImage);
            case "For Dad":
                dadImageView.setImageResource(forDadImage);
            case "Electronics":
                electronicsImageView.setImageResource(electronicsImage);
            case "Home":
                homeImageView.setImageResource(homeImage);
            default:
                Log.v("Star Wish","In the default case for some reason...");
        }
        oldCategory = newCat;
    }

    public void addButtons() {
        LinearLayout bestSellingButton = (LinearLayout )findViewById(R.id.bestSellingButton);
        LinearLayout forMomButton = (LinearLayout )findViewById(R.id.forMomButton);
        LinearLayout forDadButton = (LinearLayout )findViewById(R.id.forDadButton);
        LinearLayout electronicsButton = (LinearLayout )findViewById(R.id.electronicsButton);
        LinearLayout homeButton = (LinearLayout )findViewById(R.id.homeButton);

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
                loadGiftsForCategory("Best Selling");
            }
        });

        forMomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed forMomButton!");
                selectedCategory = "For Mom";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                momImageView.setImageResource(forMomImage_selected);
                loadGiftsForCategory("For Mom");


            }
        });
        forDadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed forDadButton!");
                selectedCategory = "For Dad";
                Log.v("Star Wish", oldCategory);
                Log.v("Star Wish", selectedCategory);

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                dadImageView.setImageResource(forDadImage_selected);
                loadGiftsForCategory("For Dad");
            }
        });
        electronicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed electronicsButton!");
                selectedCategory = "Electronics";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);
                electronicsImageView.setImageResource(electronicsImage_selected);
            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Star Wish", "Pressed homeButton!");
                selectedCategory = "Home";

                if (oldCategory.equals(selectedCategory)) {
                    // do nothing
                    Log.v("Star Wish", "Already selected that category!");

                    return;
                }
                deselectOldCategory(oldCategory,selectedCategory);

                homeImageView.setImageResource(homeImage_selected);

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

}

