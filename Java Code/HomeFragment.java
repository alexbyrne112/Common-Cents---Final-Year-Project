package com.example.alex.commoncents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.alex.commoncents.MainActivity.EnteredUsername;
import static com.example.alex.commoncents.MainActivity.UserIdentifier;
import static java.lang.Math.round;


public class HomeFragment extends Fragment {

    ListView eventList;
    //Context context = getContext();

    Event ev;
    private ArrayList<Event> events = new ArrayList<Event>();
    //ArrayAdapter<Event> adapter;
    eventAdapter adapter;
    private ProgressBar progress;
    private Firebase mRootRef;
    private String HomeTeamPerc;
    private String AwayTeamPerc;
    private String DrawPerc;
    private String amount;
    public static float Hodds, Dodds, Aodds;
    private String matchID;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.fragment_home, container, false);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        //ev = new Event();
        eventList = (ListView) home.findViewById(R.id.eventList);
        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://commoncents-190e4.firebaseio.com/Events/"+ UserIdentifier);
        adapter = new eventAdapter(getContext(), events);
        progress = (ProgressBar) home.findViewById(R.id.LoadingHome);
        progress.setVisibility(View.INVISIBLE);

        mRootRef.child("Events").child(UserIdentifier).addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                //for loop for each event the user has set.
                for(com.firebase.client.DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //setting data in event
                    ev = new Event();
                    ev.HomeTeam = (String) ds.child("HomeTeam").getValue();
                    ev.AwayTeam = (String) ds.child("AwayTeam").getValue();
                    ev.HomeScoreGuess = (String) ds.child("HomeScoreGuess").getValue();
                    ev.AwayScoreGuess = (String) ds.child("AwayScoreGuess").getValue();
                    ev.MatchDate = (String) ds.child("MatchDate").getValue();
                    ev.MatchTime = (String) ds.child("MatchTime").getValue();
                    ev.matchID = (String) ds.child("matchID").getValue();
                    events.add(ev);
                }
                eventList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                if(events.get(position).HomeTeam.matches("Brighton &amp; Hove Albion"))
                {
                    events.get(position).HomeTeam = "Brighton & Hove Albion";
                }else if (events.get(position).AwayTeam.matches("Brighton &amp; Hove Albion"))
                {
                    events.get(position).AwayTeam = "Brighton & Hove Albion";
                }
                mRootRef.child("Predictions").child("predictions").addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        for(com.firebase.client.DataSnapshot child : dataSnapshot.getChildren() ){
                            if(child.child("HomeTeam").getValue().toString().matches(events.get(position).HomeTeam) &&
                                    child.child("AwayTeam").getValue().toString().matches(events.get(position).AwayTeam))
                            {
                                //System.out.println("-----------------------------------------------------------------");
                                HomeTeamPerc = child.child("HomeWin").getValue().toString();
                                AwayTeamPerc = child.child("AwayWin").getValue().toString();
                                DrawPerc = child.child("Draw").getValue().toString();
                                Hodds = round(Float.parseFloat(HomeTeamPerc) * 100);
                                Dodds = round(Float.parseFloat(DrawPerc) * 100);
                                Aodds = round(Float.parseFloat(AwayTeamPerc) * 100);
                                Hodds = Math.round(Hodds);
                                Dodds = Math.round(Dodds);
                                Aodds = Math.round(Aodds);
                                HomeTeamPerc = String.valueOf(Hodds);
                                AwayTeamPerc = String.valueOf(Aodds);
                                DrawPerc = String.valueOf(Dodds);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast toast = Toast.makeText(getContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                mRootRef.child("Events").child(UserIdentifier).child(events.get(position).matchID).addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        amount = (String) dataSnapshot.child("amount").getValue();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast toast = Toast.makeText(getContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });

                Handler handler = new Handler();
                progress.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //open new activity function
                        openEventInfo(events.get(position).HomeTeam,
                                events.get(position).AwayTeam,
                                events.get(position).MatchTime,
                                events.get(position).MatchDate,
                                events.get(position).AwayScoreGuess,
                                events.get(position).HomeScoreGuess,
                                events.get(position).matchID) ;
                        progress.setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }
        });
        //return fragment
        return home;
    }

    public void openEventInfo(String homeTeam, String awayTeam, String matchTime, String matchDate, String awayScoreGuess, String homeScoreGuess, String matchID)
    {
        Intent intentEventInfo = new Intent(getContext(), EventInfo.class);
        intentEventInfo.putExtra("homeTeam", homeTeam);
        intentEventInfo.putExtra("awayTeam", awayTeam);
        intentEventInfo.putExtra("matchDate", matchDate);
        intentEventInfo.putExtra("matchTime", matchTime);
        intentEventInfo.putExtra("homeScoreGuess", homeScoreGuess);
        intentEventInfo.putExtra("awayScoreGuess", awayScoreGuess);
        intentEventInfo.putExtra("matchID", matchID);
        intentEventInfo.putExtra("amount", amount);
        startActivity(intentEventInfo);

    }

}
