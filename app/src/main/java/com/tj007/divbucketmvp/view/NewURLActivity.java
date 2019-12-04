package com.tj007.divbucketmvp.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class NewURLActivity extends AppCompatActivity {
    public void onCreate(Bundle saveInstanceBundle){
        super.onCreate(saveInstanceBundle);
        setContentView(R.layout.activity_newurl);
        MaterialButton button =findViewById(R.id.returnB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class Task extends AsyncTask<String,Void,Element> {
                    private Exception e;

                    @Override
                    protected Element doInBackground(String... urls) {
                        try {
                            String URL=urls[0];
                            Document doc= Jsoup.connect("https://www.baidu.com").get();
                            //Log.d("DEBUG",doc.body().outerHtml());

                            return doc.body();
                        }catch (Exception e){
                            this.e=e;
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Element element){
                        Log.d("DEBUG",element.outerHtml());
                    }
                }
                new Task().execute("https://www.baidu.com");
            }
        });

    }
}
