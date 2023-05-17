package com.example.wheresperry.data;

import com.example.wheresperry.init.MySP;
import com.google.gson.Gson;

public class DataManager {


    private static ListOfScoreRecords getScoreRecords() {
        return new Gson().fromJson(MySP.getInstance().getString("records", ""), ListOfScoreRecords.class);
    }

    public static ListOfScoreRecords getTopTenScoreRecords() {
        ListOfScoreRecords listOfResults =null;// getScoreRecords();

        if (listOfResults != null) {

            listOfResults.getListOfScoreRecords().sort((sr1, sr2) -> Integer.compare(sr2.getScore(), sr1.getScore()));
            ListOfScoreRecords topTenResults = new ListOfScoreRecords();

            for (int i = 0;i < listOfResults.getListOfScoreRecords().size() && i < 10; i++) {
                topTenResults.add(listOfResults.getListOfScoreRecords().get(i));
            }

            return topTenResults;
        }
        return null;
    }
}
