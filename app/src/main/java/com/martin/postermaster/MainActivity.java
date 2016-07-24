package com.martin.postermaster;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    PosterView posterView;
    Bitmap cover, layer1, layer2, layer3;
    List<Layer> layers = new ArrayList<>();
    Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        new Thread(runnable).start();
        posterView = (PosterView) findViewById(R.id.posterview);

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            cover = BitmapFactory.decodeResource(resources, R.drawable.cover);

            layer1 = BitmapFactory.decodeResource(resources, R.drawable.layer1);
            layers.add(new Layer(layer1, new RectF(63, 43, 478.5f, 531.5f), 5));

            layer2 = BitmapFactory.decodeResource(resources, R.drawable.layer2);
            layers.add(new Layer(layer2, new RectF(365, 440, 670, 755), -12));

            layer3 = BitmapFactory.decodeResource(resources, R.drawable.layer3);
            layers.add(new Layer(layer3, new RectF(97, 648, 366, 963.5f), 9));

            handler.sendEmptyMessage(0);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            posterView.setModle(new Modle(cover, layers));
            super.handleMessage(msg);
        }
    };

}
