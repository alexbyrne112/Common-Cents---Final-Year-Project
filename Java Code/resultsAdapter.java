package com.example.alex.commoncents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class resultsAdapter extends ArrayAdapter<ResultMatch> {

    public ArrayList<ResultMatch> resultsList;
    //constructor
    public resultsAdapter(Context context, ArrayList<ResultMatch> results) {
        super(context, R.layout.result_row, results);//change the custom row
        resultsList = results;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater fixtureInflater = LayoutInflater.from(getContext());
        convertView = fixtureInflater.inflate(R.layout.result_row, parent, false);

        ResultMatch rm = getItem(position);

        String hteamInfo = rm.HomeTeam;
        String ateamInfo = rm.AwayTeam;
        String resultDateInfo = rm.rDate;
        String HscoreInfo = rm.homeScore;
        String AscoreInfo = rm.awayScore;
        TextView teamA = (TextView) convertView.findViewById(R.id.homeTeam);
        TextView teamB = (TextView) convertView.findViewById(R.id.awayTeam);
        TextView matchDate = (TextView) convertView.findViewById(R.id.matchDate);
        TextView hTeamScore = (TextView) convertView.findViewById(R.id.homeScore);
        TextView aTeamScore = (TextView) convertView.findViewById(R.id.awayScore);
        TextView vers = (TextView) convertView.findViewById(R.id.versus);

        teamA.setText(hteamInfo);
        vers.setText("V");
        teamB.setText(ateamInfo);
        matchDate.setText("    "+resultDateInfo);
        hTeamScore.setText(HscoreInfo);
        aTeamScore.setText("  "+AscoreInfo);


        return convertView;
    }
}
