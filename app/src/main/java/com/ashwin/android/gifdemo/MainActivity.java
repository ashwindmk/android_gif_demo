package com.ashwin.android.gifdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final AnimatedImageDrawable gif = (AnimatedImageDrawable) ImageDecoder.decodeDrawable(ImageDecoder.createSource(getResources(), R.drawable.error_99));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imgView = (ImageView) findViewById(R.id.gif_imageview);
                                imgView.setVisibility(View.VISIBLE);
                                imgView.setImageDrawable(gif);
                                gif.start();
                            }
                        });
                    } catch (IOException e) {
                        Log.e("gif-demo", "Exception while decoding GIF drawable", e);
                    }
                }
            }).start();
        } else {
            // Using WebView for versions below Android 9
            String gif = "<img src=\"error_99.gif\" >";

            String html = "<html style=\"margin: auto; width: fit-content;\">\n" +
                        "<body style=\"margin: 0;\">\n" +
                            gif + "\n" +
                        "</body>\n" +
                    "</html>";

            WebView webView = (WebView) findViewById(R.id.gif_webview);
            webView.setVisibility(View.VISIBLE);

            webView.loadDataWithBaseURL("file:///android_res/drawable/", html, "text/html", "utf-8", null);

            // Set WebView width
            final BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.drawable.error_99, opt);
            final float scale = getResources().getDisplayMetrics().density;
            webView.getLayoutParams().width = (int) (opt.outWidth * scale);
        }
    }
}
