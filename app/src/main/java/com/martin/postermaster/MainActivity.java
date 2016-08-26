package com.martin.postermaster;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.martin.poster.Layer;
import com.martin.poster.Model;
import com.martin.poster.ModelView;
import com.martin.poster.OnLayerSelectListener;
import com.martin.poster.PosterView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String Tag = "MainActivity";
    PosterView posterView;
    View menu;
    List<Layer> layers = new ArrayList<>();
    Resources resources;
    RequestManager requestManager;
    Bitmap cover, layer1, layer2, layer3;

    static final int MaxCoverWidth = 720;
    static final int MaxLayerWidth = 400;

    private RecyclerView recyclerView;
    private FiltersAdapter filtersAdapter;
    private List<float[]> filters = new ArrayList<>();

    private Layer selectdLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resources = getResources();
        requestManager = Glide.with(this);
        posterView = (PosterView) findViewById(R.id.posterview);
        menu = getLayoutInflater().inflate(R.layout.filter_menu, null);
        recyclerView = (RecyclerView) menu.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        inItFilters();
        filtersAdapter = new FiltersAdapter(requestManager, getLayoutInflater(), filters);

        filtersAdapter.setOnItemSelectListener(onItemSelectListener);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(filtersAdapter);
        posterView.addMenuInit(menu, Utils.getScreenWidth(getApplicationContext()) - Utils.dpToPx(getApplicationContext(), 100)
                , Utils.dpToPx(getApplicationContext(), 60));

        new Thread(runnable).start();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                cover = requestManager.load(R.drawable.cover).asBitmap().into(MaxCoverWidth, MaxCoverWidth).get();
                layer1 = requestManager.load(R.drawable.layer1).asBitmap().into(MaxLayerWidth, MaxLayerWidth).get();
                layer2 = requestManager.load(R.drawable.layer2).asBitmap().into(MaxLayerWidth, MaxLayerWidth).get();
                layer3 = requestManager.load(R.drawable.layer3).asBitmap().into(MaxLayerWidth, MaxLayerWidth).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            layers.add(new Layer(layer1, 5)
                    .markPoint(63, 43).markPoint(495, 80)
                    .markPoint(454, 552).markPoint(21, 515).build());

            layers.add(new Layer(layer2, -12)
                    .markPoint(362, 439).markPoint(653, 372)
                    .markPoint(724, 685).markPoint(434, 751).build());

            layers.add(new Layer(layer3, 9)
                    .markPoint(95, 648).markPoint(366, 692)
                    .markPoint(316, 988).markPoint(45, 943).build());

            handler.sendEmptyMessage(0);
        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            posterView.setModel(new Model(cover, layers));
            posterView.getModelView().setOnLayerSelectListener(onLayerSelectListener);
            super.handleMessage(msg);
        }
    };

    FiltersAdapter.OnItemSelectListener onItemSelectListener = new FiltersAdapter.OnItemSelectListener() {
        @Override
        public void selected(float[] filter) {
            if (selectdLayer != null) {
                if (filter != null) {
                    Log.i(Tag, "onItemSelectListener");
                    Bitmap bitmap = ColorFilter.setColorMatrix(selectdLayer.getLayer(), filter, false);
                    selectdLayer.setFilterLayer(bitmap);
                    posterView.getModelView().invalidate();
                } else {
                    selectdLayer.clearFilter();
                    posterView.getModelView().invalidate();
                }
            }
        }
    };

    OnLayerSelectListener onLayerSelectListener = new OnLayerSelectListener() {
        @Override
        public void onSelected(Layer layer) {
            selectdLayer = layer;
            posterView.showMenu(layer);
        }

        @Override
        public void dismiss(Layer layer) {

            if (selectdLayer == layer) {//怕有冲突加额外的判断
                selectdLayer = null;
                posterView.dissMenu();
            }
        }
    };


    private void inItFilters() {
        filters.add(ColorFilter.colormatrix_heibai);
        filters.add(ColorFilter.colormatrix_fugu);
        filters.add(ColorFilter.colormatrix_gete);
        filters.add(ColorFilter.colormatrix_chuan_tong);
        filters.add(ColorFilter.colormatrix_danya);
        filters.add(ColorFilter.colormatrix_guangyun);
        filters.add(ColorFilter.colormatrix_fanse);
        filters.add(ColorFilter.colormatrix_hepian);
        filters.add(ColorFilter.colormatrix_huajiu);
        filters.add(ColorFilter.colormatrix_jiao_pian);
        filters.add(ColorFilter.colormatrix_landiao);
        filters.add(ColorFilter.colormatrix_langman);
        filters.add(ColorFilter.colormatrix_ruise);
        filters.add(ColorFilter.colormatrix_menghuan);
        filters.add(ColorFilter.colormatrix_qingning);
        filters.add(ColorFilter.colormatrix_yese);
    }
}
