package com.example.alex.commoncents;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.VIBRATOR_SERVICE;
import static java.lang.Math.round;

public class FunctionFragment extends Fragment {
    public static ListView fetchedFixtures;
    public static String weekLater;
    private String HomeTeamPerc;
    private String AwayTeamPerc;
    private String DrawPerc;
    private float Hodds, Dodds, Aodds;
    public static int fixPos;
    Context context = getActivity();
    private Firebase mRootRef;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View function = inflater.inflate(R.layout.fragment_function, container, false);
        fetchedFixtures = (ListView) function.findViewById(R.id.fixtureList);
        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        //code to get a date for days later
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        Date weeks = calendar.getTime();
        weekLater = weeks.toString();
        weekLater = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(weeks);
        progress = (ProgressBar) function.findViewById(R.id.Loading);
        progress.setVisibility(View.INVISIBLE);

        //call the getLeague data Async Task
        final fetchLeagues getLeagueData = new fetchLeagues(getActivity());
        getLeagueData.execute(getActivity());

        fetchedFixtures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                fixPos = position;
                final FixtureMatch fm = getLeagueData.fixtures.get(fixPos);
                mRootRef.child("Predictions").child("predictions").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren() ){
                            if(child.child("HomeTeam").getValue().toString().matches(fm.HomeTeam) &&
                                    child.child("AwayTeam").getValue().toString().matches(fm.AwayTeam))
                            {
                                System.out.println("-----------------------------------------------------------------");
                                HomeTeamPerc = child.child("HomeWin").getValue().toString();
                                AwayTeamPerc = child.child("AwayWin").getValue().toString();
                                DrawPerc = child.child("Draw").getValue().toString();
                                Hodds = round(Float.parseFloat(HomeTeamPerc) * 100);
                                Dodds = round(Float.parseFloat(DrawPerc) * 100);
                                Aodds = round(Float.parseFloat(AwayTeamPerc) * 100);
                                HomeTeamPerc = String.valueOf(Hodds);
                                AwayTeamPerc = String.valueOf(Aodds);
                                DrawPerc = String.valueOf(Dodds);
                                System.out.println(HomeTeamPerc + "\n" + AwayTeamPerc + "\n" + DrawPerc + "\n-------------");
                                //inputOdds(finalHome_team_name,finalAway_team_name);
                                //System.out.println(Hodds +" "+Dodds +" "+Aodds +" "+"---------------------------------------");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Handler handler = new Handler();
                progress.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        openFixtureInfo(HomeTeamPerc, AwayTeamPerc, DrawPerc);
                        progress.setVisibility(View.INVISIBLE);
                    }
                }, 750);
            }
        });

        return function;
    }

    public void openFixtureInfo(String homeTeamPerc, String awayTeamPerc, String drawPerc)
    {
        Intent intentFixtureInfo = new Intent(getContext(), FixturesInfo.class);
        intentFixtureInfo.putExtra("homeTeamPerc", homeTeamPerc);
        intentFixtureInfo.putExtra("awayTeamPerc", awayTeamPerc);
        intentFixtureInfo.putExtra("drawPerc", drawPerc);
        startActivity(intentFixtureInfo);
    }
}
