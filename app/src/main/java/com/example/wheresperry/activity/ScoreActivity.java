package com.example.wheresperry.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.wheresperry.R;
import com.example.wheresperry.data.DataManager;
import com.example.wheresperry.data.ListOfScoreRecords;
import com.example.wheresperry.data.ScoreRecord;
import com.example.wheresperry.fragment.ItemFragment;
import com.example.wheresperry.fragment.MapsFragment;
import com.example.wheresperry.init.MyGPS;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

public class ScoreActivity extends AppCompatActivity {


    private MaterialButton score_BTN_close;
    private ItemFragment fragment_list;
    private MapsFragment fragment_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
        initFragments();
        loadFragments();
    }

//    private void initFragments() {
//        fragment_map = new MapsFragment();
//        fragment_map.setMapLocation( MyGPS.getInstance().getLatitude(), MyGPS.getInstance().getLongitude(),"michaella");
//
//
//        fragment_list = new ItemFragment();
//        fragment_list.setCallback_list(callback_list);
//    }
private void initFragments() {
    fragment_map = new MapsFragment();
    fragment_map.setCallback_map(callback_map);


    fragment_list = new ItemFragment();
    fragment_list.setCallback_list(callback_list);
}
    Callback_List callback_list = new Callback_List() {
        @Override
        public ListOfScoreRecords getTopTenScoreRecords() {
            return DataManager.getTopTenScoreRecords();
        }
    };

    public interface Callback_List {
        ListOfScoreRecords getTopTenScoreRecords();
    }

    private void loadFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.score_LAY_list, fragment_list)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.score_LAY_map, fragment_map)
                .commit();
    }

    private void findViews() {
        score_BTN_close = findViewById(R.id.score_BTN_close);
    }


    private void initViews() {

        score_BTN_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public interface Callback_Map {
        void setMarkers(GoogleMap map);
    }
    public void zoomToLocation(LatLng location) {
        fragment_map.zoomToLocation(location);
    }

    Callback_Map callback_map = new Callback_Map() {
        @Override
        public void setMarkers(GoogleMap map) {
            map.clear();
            ListOfScoreRecords topTenScoreRecords = DataManager.getTopTenScoreRecords();
            if (topTenScoreRecords != null) {
                for (int i = 0; i < topTenScoreRecords.size(); i++) {
                    ScoreRecord result = topTenScoreRecords.get(i);
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(
                                    result.getLatitude(),
                                    result.getLongitude()))
                            .title("" + i));
                }
            }
        }
    };



}