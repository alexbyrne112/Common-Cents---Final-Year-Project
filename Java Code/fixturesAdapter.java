package com.example.alex.commoncents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class fixturesAdapter extends ArrayAdapter<FixtureMatch> {

    public ArrayList<FixtureMatch> fixtureList;

    public fixturesAdapter(Context context, ArrayList<FixtureMatch> fixtures) {
        super(context, R.layout.fixture_row, fixtures);
        fixtureList = fixtures;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater fixtureInflater = LayoutInflater.from(getContext());
        convertView = fixtureInflater.inflate(R.layout.fixture_row, parent, false);

        FixtureMatch fm = getItem(position);

        String hteamInfo = fm.HomeTeam;
        String ateamInfo = fm.AwayTeam;
        String dateInfo = fm.matchDate;
        String timeInfo = fm.matchTime;
        TextView teamA = (TextView) convertView.findViewById(R.id.homeTeam);
        TextView teamB = (TextView) convertView.findViewById(R.id.awayTeam);
        TextView KOdate = (TextView) convertView.findViewById(R.id.KickOffdate);
        TextView KOtime = (TextView) convertView.findViewById(R.id.KickOfftime);
        TextView vers = (TextView) convertView.findViewById(R.id.versus);

        teamA.setText(hteamInfo);
        vers.setText("V");
        teamB.setText(ateamInfo);
        KOdate.setText("              "+dateInfo);
        KOtime.setText(timeInfo);


        return convertView;
    }
}
