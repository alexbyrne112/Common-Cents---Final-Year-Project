package com.example.alex.commoncents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alex.commoncents.ResultsFragment.resPos;
import static com.example.alex.commoncents.fetchResults.resultsArray;

public class ResultsInfo extends AppCompatActivity {

    private TextView homeTeam;
    private TextView versus;
    private TextView awayTeam;
    private TextView hGoals;
    private TextView goalsText;
    private TextView aGoals;
    private ProgressBar goalsBar;
    private TextView hshots;
    private TextView shotsText;
    private TextView ashots;
    private ProgressBar shotsBar;
    private TextView hpos;
    private TextView posText;
    private TextView apos;
    private ProgressBar posBar;
    private TextView hfoul;
    private TextView foulText;
    private TextView afoul;
    private ProgressBar foulBar;
    private TextView hcorner;
    private TextView cornerText;
    private TextView acorner;
    private ProgressBar cornerBar;
    private TextView hoffside;
    private TextView offsideText;
    private TextView aoffside;
    private ProgressBar offsideBar;

    private String home_shots;
    private String away_shots;
    private String home_pos;
    private String away_pos;
    private String home_corners;
    private String away_corners;
    private String home_fouls;
    private String away_fouls;
    private String home_offside;
    private String away_offside;

    private Firebase mRootRef;
    private ImageView crestHome, crestAway;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_info);

        homeTeam = (TextView) findViewById(R.id.homeTeam);
        versus = (TextView) findViewById(R.id.versus);
        awayTeam = (TextView) findViewById(R.id.awayTeam);
        hGoals = (TextView) findViewById(R.id.textView1);
        goalsText = (TextView) findViewById(R.id.textView1_1);
        aGoals = (TextView) findViewById(R.id.textView1_2);
        goalsBar = (ProgressBar) findViewById(R.id.progressBar1);
        hshots = (TextView) findViewById(R.id.textView2);
        shotsText = (TextView) findViewById(R.id.textView2_2);
        ashots = (TextView) findViewById(R.id.textView2_3);
        shotsBar = (ProgressBar) findViewById(R.id.progressBar2);
        hpos = (TextView) findViewById(R.id.textView3);
        posText = (TextView) findViewById(R.id.textView3_2);
        apos = (TextView) findViewById(R.id.textView3_3);
        posBar = (ProgressBar) findViewById(R.id.progressBar3);
        hfoul = (TextView) findViewById(R.id.textView4);
        foulText = (TextView) findViewById(R.id.textView4_2);
        afoul = (TextView) findViewById(R.id.textView4_3);
        foulBar = (ProgressBar) findViewById(R.id.progressBar4);
        hcorner = (TextView) findViewById(R.id.textView5);
        cornerText = (TextView) findViewById(R.id.textView5_2);
        acorner = (TextView) findViewById(R.id.textView5_3);
        cornerBar = (ProgressBar) findViewById(R.id.progressBar5);
        hoffside = (TextView) findViewById(R.id.textView6);
        offsideText = (TextView) findViewById(R.id.textView6_2);
        aoffside = (TextView) findViewById(R.id.textView6_3);
        offsideBar = (ProgressBar) findViewById(R.id.progressBar6);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        crestHome = (ImageView) findViewById(R.id.crestHome);
        crestAway = (ImageView) findViewById(R.id.crestAway);


         JSONObject resultsObject = null;
        try {
             resultsObject = (JSONObject) resultsArray.get(resultsArray.length() -resPos -1);
             JSONArray stats = (JSONArray) resultsObject.get("statistics");


             String home_team_name = (String) resultsObject.get("match_hometeam_name");
             String away_team_name = (String) resultsObject.get("match_awayteam_name");
            if(home_team_name.matches("Brighton &amp; Hove Albion"))
            {
                home_team_name = "Brighton & Hove Albion";
            }else if (away_team_name.matches("Brighton &amp; Hove Albion"))
            {
                away_team_name = "Brighton & Hove Albion";
            }
             String home_goals = (String) resultsObject.get("match_hometeam_score");
             final String away_goals = (String) resultsObject.get("match_awayteam_score");

            final String finalAway_team_name = away_team_name;
            final String finalHome_team_name = home_team_name;
            mRootRef.child("Crests").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String homeurl = (String) dataSnapshot.child(finalHome_team_name).getValue();
                    String awayurl = (String) dataSnapshot.child(finalAway_team_name).getValue();
                    Picasso.get().load(homeurl).into(crestHome);
                    Picasso.get().load(awayurl).into(crestAway);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                    toast.show();
                }
            });

            System.out.println(stats);

            for (int i = 0; i < stats.length(); i++) {
                if(i == 0)
                {
                    //Shots on target
                    home_shots = (String) stats.getJSONObject(i).getString("home");
                    away_shots = (String) stats.getJSONObject(i).getString("away");
                }
                if(i == 2)
                {
                    //possession
                    home_pos = (String) stats.getJSONObject(i).getString("home");
                    away_pos = (String) stats.getJSONObject(i).getString("away");
                }
                if(i == 3)
                {
                    //corners
                    home_corners = (String) stats.getJSONObject(i).getString("home");
                    away_corners = (String) stats.getJSONObject(i).getString("away");
                }
                if(i == 4)
                {
                    home_offside = (String) stats.getJSONObject(i).getString("home");
                    away_offside = (String) stats.getJSONObject(i).getString("away");
                }
                if(i == 5)
                {
                    //fouls
                    home_fouls = (String) stats.getJSONObject(i).getString("home");
                    away_fouls = (String) stats.getJSONObject(i).getString("away");
                }
            }

            homeTeam.setText(home_team_name);
            versus.setText("V");
            awayTeam.setText(away_team_name);

            hGoals.setText(home_goals);
            goalsText.setText("Goals");
            aGoals.setText(away_goals);
            float gpTop = Integer.parseInt(home_goals);
            float  gpBottom = Integer.parseInt(home_goals) + Integer.parseInt(away_goals);
            if(gpTop == 0 && gpBottom == 0) {
                goalsBar.setProgress(50);
                goalsBar.setScaleY(2f);
                goalsBar.setSecondaryProgress(100);
            }else{
                float goalProg = (gpTop/gpBottom)*100;
                if(goalProg == 100)
                {
                    goalProg = 98;
                }
                else if (goalProg == 0)
                {
                    goalProg = 2;
                }
                goalsBar.setProgress((int)goalProg);
                goalsBar.setScaleY(2f);
            }

            hshots.setText(home_shots);
            shotsText.setText("Shots on Target");
            ashots.setText(away_shots);
            float spTop = Integer.parseInt(home_shots);
            float  spBottom = Integer.parseInt(home_shots) + Integer.parseInt(away_shots);
            if(spTop == 0 && spBottom == 0) {
                shotsBar.setProgress(50);
                shotsBar.setScaleY(2f);
            }else{
                float shotsProg = (spTop/spBottom)*100;
                if(shotsProg == 100)
                {
                    shotsProg = 98;
                }
                else if (shotsProg == 0)
                {
                    shotsProg = 2;
                }
                shotsBar.setProgress((int)shotsProg);
                shotsBar.setScaleY(2f);
            }

            hpos.setText(home_pos);
            posText.setText("Possession");
            apos.setText(away_pos);
            int possession = Integer.parseInt(home_pos);
            posBar.setProgress(possession);
            posBar.setScaleY(2f);

            hfoul.setText(home_fouls);
            foulText.setText("Fouls");
            afoul.setText(away_fouls);
            float fpTop = Integer.parseInt(home_fouls);
            float  fpBottom = Integer.parseInt(home_fouls) + Integer.parseInt(away_fouls);
            if(fpTop == 0 && fpBottom == 0) {
                foulBar.setProgress(50);
                foulBar.setScaleY(2f);
            }else {
                float foulsProg = (fpTop / fpBottom) * 100;
                if(foulsProg == 100)
                {
                    foulsProg = 98;
                }
                else if (foulsProg == 0)
                {
                    foulsProg = 2;
                }
                foulBar.setProgress((int) foulsProg);
                foulBar.setScaleY(2f);
            }

            hcorner.setText(home_corners);
            cornerText.setText("Corners");
            acorner.setText(away_corners);
            float cpTop = Integer.parseInt(home_corners);
            float  cpBottom = Integer.parseInt(home_corners) + Integer.parseInt(away_corners);
            if(cpTop == 0 && cpBottom == 0) {
                cornerBar.setProgress(50);
                cornerBar.setScaleY(2f);
            }else {
                float cornersProg = (cpTop / cpBottom) * 100;
                if(cornersProg == 100)
                {
                    cornersProg = 98;
                }
                else if (cornersProg == 0)
                {
                    cornersProg = 2;
                }
                cornerBar.setProgress((int) cornersProg);
                cornerBar.setScaleY(2f);
            }

            hoffside.setText(home_offside);
            offsideText.setText("Offsides");
            aoffside.setText(away_offside);
            float opTop = Integer.parseInt(home_offside);
            float  opBottom = Integer.parseInt(home_offside) + Integer.parseInt(away_offside);
            if(opTop == 0 && opBottom == 0) {
                offsideBar.setProgress(50);
                offsideBar.setScaleY(2f);
            }else {
                float offProg = (opTop / opBottom) * 100;
                if(offProg == 100)
                {
                    offProg = 98;
                }
                else if (offProg == 0)
                {
                    offProg = 2;
                }
                offsideBar.setProgress((int) offProg);
                offsideBar.setScaleY(2f);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch(NumberFormatException e){
            e.printStackTrace();
        }
    }
}
