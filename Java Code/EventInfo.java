package com.example.alex.commoncents;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Color.rgb;
import static com.example.alex.commoncents.HomeFragment.Aodds;
import static com.example.alex.commoncents.HomeFragment.Dodds;
import static com.example.alex.commoncents.HomeFragment.Hodds;
import static com.example.alex.commoncents.MainActivity.EnteredUsername;
import static com.example.alex.commoncents.MainActivity.UserIdentifier;
import static com.example.alex.commoncents.UserArea.UArefresh;

public class EventInfo extends AppCompatActivity {

    private TextView homeTeam, versus, awayTeam, selectedTeam, PredHomeTeam, PredAwayTeam, MatchDate, HomeScoreGuess, AwayScoreGuess, MatchTime, yourPredictionText;
    private TextView homeScoreLive, awayScoreLive, LiveTime;
    private ImageView crest, crestHome, crestAway;
    private Firebase mRootRef;
    private String url, HomeTeam, AwayTeam, matchID;
    private CardView deleteButton;
    private boolean isMatchLive = false;




////////////////////////////Call back method to get live scores up to date in the activity
    Handler h = new Handler();
    int delay = 30000; //milliseconds
    Runnable runnable;

    @Override
    protected void onResume() {
        final Intent intent = getIntent();
        //start handler as activity become visible
        h.postDelayed( runnable = new Runnable() {
            public void run() {

                final String todaysDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                String liveScoresURL = "https://apifootball.com/api/?action=get_events&league_id=62&from="+todaysDate+"&to="+todaysDate+"&match_live=1&APIkey=894774b4eac49f8b7a14425609d7d736048e81188d446a3fbf47630a5c906245";
                RequestQueue liveScores = Volley.newRequestQueue(getApplicationContext());

                StringRequest request = new StringRequest(Request.Method.GET, liveScoresURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //System.out.println();
                                try {
                                    JSONArray liveScoreArray = new JSONArray(response);
                                    for(int i =0; i < liveScoreArray.length(); i++)
                                    {
                                        JSONObject liveMatch = liveScoreArray.getJSONObject(i);
                                        String tempMatchID = (String) liveMatch.get("match_id");
                                        System.out.println(tempMatchID);
                                        if (tempMatchID.matches(matchID))
                                        {
                                            isMatchLive = true;
                                            String liveAwayScore = (String) liveMatch.get("match_awayteam_score");
                                            String liveHomeScore = (String) liveMatch.get("match_hometeam_score");
                                            String liveTime = (String) liveMatch.get("match_status");
                                            homeScoreLive.setText(liveHomeScore);
                                            awayScoreLive.setText(liveAwayScore);
                                            LiveTime.setText(liveTime);
                                            if(isMatchLive == true)
                                            {
                                                String homeScore = homeScoreLive.getText().toString();
                                                String awayScore = awayScoreLive.getText().toString();

                                                if(intent.getStringExtra("homeScoreGuess").matches(homeScore))
                                                {
                                                    HomeScoreGuess.setTextColor(Color.parseColor("#32CD32"));
                                                }else
                                                {
                                                    HomeScoreGuess.setTextColor(Color.parseColor("#FF0000"));
                                                }

                                                if(intent.getStringExtra("awayScoreGuess").matches(awayScore))
                                                {
                                                    AwayScoreGuess.setTextColor(Color.parseColor("#32CD32"));
                                                }else
                                                {
                                                    AwayScoreGuess.setTextColor(Color.parseColor("#FF0000"));
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                liveScores.add(request);


                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }









    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        final Intent intent = getIntent();

        HomeTeam = intent.getStringExtra("homeTeam");
        AwayTeam = intent.getStringExtra("awayTeam");
        matchID = intent.getStringExtra("matchID");

        crest = (ImageView)findViewById(R.id.crest);
        selectedTeam = (TextView)findViewById(R.id.selectedTeam);
        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");

        homeTeam = (TextView) findViewById(R.id.homeTeam);
        versus = (TextView) findViewById(R.id.versus);
        awayTeam = (TextView) findViewById(R.id.awayTeam);
        PredHomeTeam = (TextView) findViewById(R.id.PreHomeTeam);
        PredAwayTeam = (TextView) findViewById(R.id.PredAwayTeam);
        MatchDate = (TextView) findViewById(R.id.matchDate);
        HomeScoreGuess = (TextView) findViewById(R.id.homeScoreGuess);
        AwayScoreGuess = (TextView) findViewById(R.id.awayScoreGuess);
        MatchTime = (TextView) findViewById(R.id.matchTime);
        deleteButton = (CardView) findViewById(R.id.deleteButton);
        crestHome = (ImageView) findViewById(R.id.crestHome);
        crestAway = (ImageView) findViewById(R.id.crestAway);
        yourPredictionText = (TextView) findViewById(R.id.yourPredictionText);

        homeScoreLive = (TextView) findViewById(R.id.homeLiveScore);
        awayScoreLive = (TextView) findViewById(R.id.awayLiveScore);
        LiveTime = (TextView) findViewById(R.id.LiveTime);
        final String todaysDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        homeTeam.setText(intent.getStringExtra("homeTeam"));
        versus.setText("V");
        awayTeam.setText(intent.getStringExtra("awayTeam"));
        yourPredictionText.setText("Your Prediction: €" + intent.getStringExtra("amount"));

        //initial retreival of live scores
        String liveScoresURL = "https://apifootball.com/api/?action=get_events&league_id=62&from="+todaysDate+"&to="+todaysDate+"&match_live=1&APIkey=894774b4eac49f8b7a14425609d7d736048e81188d446a3fbf47630a5c906245";
        RequestQueue liveScores = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.GET, liveScoresURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println();
                        try {
                            JSONArray liveScoreArray = new JSONArray(response);
                            for(int i =0; i < liveScoreArray.length(); i++)
                            {
                                JSONObject liveMatch = liveScoreArray.getJSONObject(i);
                                String tempMatchID = (String) liveMatch.get("match_id");
                                System.out.println(tempMatchID);
                                if (tempMatchID.matches(matchID))
                                {
                                    isMatchLive = true;
                                    String liveAwayScore = (String) liveMatch.get("match_awayteam_score");
                                    String liveHomeScore = (String) liveMatch.get("match_hometeam_score");
                                    String liveTime = (String) liveMatch.get("match_status");
                                    homeScoreLive.setText(liveHomeScore);
                                    awayScoreLive.setText(liveAwayScore);
                                    LiveTime.setText(liveTime);
                                    if(isMatchLive == true)
                                    {
                                        String homeScore = homeScoreLive.getText().toString();
                                        String awayScore = awayScoreLive.getText().toString();

                                        if(intent.getStringExtra("homeScoreGuess").matches(homeScore))
                                        {
                                            HomeScoreGuess.setTextColor(Color.parseColor("#32CD32"));
                                        }else
                                        {
                                            HomeScoreGuess.setTextColor(Color.parseColor("#FF0000"));
                                        }

                                        if(intent.getStringExtra("awayScoreGuess").matches(awayScore))
                                        {
                                            AwayScoreGuess.setTextColor(Color.parseColor("#32CD32"));
                                        }else
                                        {
                                            AwayScoreGuess.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        liveScores.add(request);


        AnimatedPieView oddsChart = findViewById(R.id.pieChart);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90);
        Toast toast = Toast.makeText(getApplicationContext(), Hodds + "     " + Aodds+ "     " + Dodds + "-----------------", Toast.LENGTH_LONG);
        toast.show();
        config.addData(new SimplePieInfo(Aodds, Color.parseColor("#ff99ff"), intent.getStringExtra("awayTeam")));
        config.addData(new SimplePieInfo(Dodds, Color.parseColor("#b3b3b3"), "Draw"));
        config.addData(new SimplePieInfo(Hodds, Color.parseColor("#33ccff"), intent.getStringExtra("homeTeam")));
        oddsChart.applyConfig(config);
        config.duration(400).drawText(true).textSize(30).strokeWidth(150).splitAngle(2);
        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                final String team = pieInfo.getDesc();
                String odds = String.valueOf(pieInfo.getValue());
                final String finalOdds = odds.substring(0, (odds.length() -2));
                if(!team.matches("Draw")) {
                    mRootRef.child("Crests").child(team).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            url = dataSnapshot.getValue().toString();
                            Picasso.get().load(url).into(crest);
                            selectedTeam.setText(team + ": " + finalOdds+ "% Chance of winning");
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
                else
                {
                    mRootRef.child("Crests").child(team).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            url = dataSnapshot.getValue().toString();
                            Picasso.get().load(url).into(crest);
                            selectedTeam.setText(finalOdds + "% Chance of a Draw");
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            }
        });
        oddsChart.start();



        mRootRef.child("Crests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String homeurl = (String) dataSnapshot.child(HomeTeam).getValue();
                String awayurl = (String) dataSnapshot.child(AwayTeam).getValue();
                Picasso.get().load(homeurl).into(crestHome);
                Picasso.get().load(awayurl).into(crestAway);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        PredHomeTeam.setText(intent.getStringExtra("homeTeam"));
        PredAwayTeam.setText(intent.getStringExtra("awayTeam"));
        MatchDate.setText(intent.getStringExtra("matchDate"));
        HomeScoreGuess.setText(intent.getStringExtra("homeScoreGuess"));
        AwayScoreGuess.setText(intent.getStringExtra("awayScoreGuess"));;
        MatchTime.setText(intent.getStringExtra("matchTime"));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                LayoutInflater LI = LayoutInflater.from(EventInfo.this);
                View prompt = LI.inflate(R.layout.info_change, null);
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(EventInfo.this);
                alertdialog.setView(prompt);
                TextView changePrompt = (TextView) prompt.findViewById(R.id.changer);
                changePrompt.setText("Are You Sure You Want To Delete this Prediction?");
                alertdialog.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mRootRef.child("Events").child(UserIdentifier).child(matchID).removeValue();
                                UArefresh.finish();
                                finish();
                                startActivity(UArefresh.getIntent());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertdialog.create();
                alertdialog.show();
            }
        });
    }
}