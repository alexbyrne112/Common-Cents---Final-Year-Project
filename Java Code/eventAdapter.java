package com.example.alex.commoncents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class eventAdapter extends ArrayAdapter<Event> {

    public ArrayList<Event> eventsList;

    public eventAdapter(Context context, ArrayList<Event> setEvents) {
        super(context, R.layout.event_row, setEvents);//change the custom row
        eventsList = setEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater eventInflater = LayoutInflater.from(getContext());
        convertView = eventInflater.inflate(R.layout.event_row, parent, false);

        Event ev = eventsList.get(position);

        String matchID = ev.matchID;
        String hteamInfo = ev.HomeTeam;
        String ateamInfo = ev.AwayTeam;
        String eventDate = ev.MatchDate;
        String eventTime = ev.MatchTime;
        String HscoreGuess = ev.HomeScoreGuess;
        String AscoreGuess = ev.AwayScoreGuess;
        TextView teamA = (TextView) convertView.findViewById(R.id.homeTeam);
        TextView teamB = (TextView) convertView.findViewById(R.id.awayTeam);
        TextView matchDate = (TextView) convertView.findViewById(R.id.matchDate);
        TextView matchTime = (TextView) convertView.findViewById(R.id.matchTime);
        TextView hTeamGuess = (TextView) convertView.findViewById(R.id.homeScoreGuess);
        TextView aTeamGuess = (TextView) convertView.findViewById(R.id.awayScoreGuess);
        TextView vers = (TextView) convertView.findViewById(R.id.versus);

        if(hteamInfo.matches("Brighton &amp; Hove Albion"))
        {
            hteamInfo = "Brighton & Hove Albion";
        }else if (ateamInfo.matches("Brighton &amp; Hove Albion"))
        {
            ateamInfo = "Brighton & Hove Albion";
        }

        teamA.setText(hteamInfo);
        vers.setText("V");
        teamB.setText(ateamInfo);
        matchDate.setText(eventDate);
        matchTime.setText(eventTime);
        hTeamGuess.setText(HscoreGuess);
        aTeamGuess.setText(AscoreGuess);

        return convertView;
    }
}
