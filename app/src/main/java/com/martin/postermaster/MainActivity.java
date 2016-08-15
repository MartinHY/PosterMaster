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
            layers.add(new Layer(layer1, 5)
                    .markPoint(63, 43).markPoint(495, 80)
                    .markPoint(454, 552).markPoint(21, 515).build());

            layer2 = BitmapFactory.decodeResource(resources, R.drawable.layer2);
            layers.add(new Layer(layer2, -12)
                    .markPoint(362, 439).markPoint(653, 372)
                    .markPoint(724, 685).markPoint(434, 751).build());

            layer3 = BitmapFactory.decodeResource(resources, R.drawable.layer3);
            layers.add(new Layer(layer3, 9)
                    .markPoint(95, 648).markPoint(366, 692)
                    .markPoint(316, 988).markPoint(45, 943).build());

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
