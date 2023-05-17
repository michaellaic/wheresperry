package com.example.wheresperry.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wheresperry.MyItemRecyclerViewAdapter;
import com.example.wheresperry.R;
import com.example.wheresperry.activity.ScoreActivity;
import com.example.wheresperry.data.ListOfScoreRecords;
import com.example.wheresperry.data.ScoreRecord;
import com.example.wheresperry.placeholder.PlaceholderContent;
import com.example.wheresperry.utilities.ScoreRecordAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    private ListView fragmentList_LIST_Scores;
    private ScoreActivity.Callback_List callback_list;


    public void setCallback_list(ScoreActivity.Callback_List callback_list) {
        this.callback_list = callback_list;
    }

    private void initViews() {
        ListOfScoreRecords data = callback_list.getTopTenScoreRecords();
        if (data != null) {
            List<ScoreRecord> scoreRecords = data.getListOfScoreRecords();
            ScoreRecordAdapter adapter = new ScoreRecordAdapter(getActivity(), android.R.layout.simple_list_item_1, scoreRecords);
            fragmentList_LIST_Scores.setAdapter(adapter);

        }
        fragmentList_LIST_Scores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScoreRecord scoreRecord = (ScoreRecord) adapterView.getItemAtPosition(i);
                LatLng location = new LatLng(scoreRecord.getLatitude(), scoreRecord.getLongitude());
                ((ScoreActivity) getActivity()).zoomToLocation(location);
            }
        });
    }


    private void findViews(View view) {
        fragmentList_LIST_Scores = view.findViewById(R.id.fragmentList_LIST_Scores);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        findViews(view);
        initViews();
        return view;
    }
}