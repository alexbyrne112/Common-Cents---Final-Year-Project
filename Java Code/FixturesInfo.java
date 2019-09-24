package com.example.alex.commoncents;

import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import static com.example.alex.commoncents.FunctionFragment.fixPos;
import static com.example.alex.commoncents.MainActivity.EnteredUsername;
import static com.example.alex.commoncents.MainActivity.UserIdentifier;
import static com.example.alex.commoncents.ProfileFragment.newUsername;
import static com.example.alex.commoncents.fetchLeagues.fixturesArray;
import static java.lang.Math.round;

public class FixturesInfo extends AppCompatActivity {

    private Firebase mRootRef;
    private TextView homeTeam;
    private TextView versus;
    private TextView awayTeam;
    private EditText homeScore;
    private EditText awayScore;
    private EditText setPrice;
    private TextView homeScoreText;
    private TextView awayScoreText;
    private TextView priceText;
    private CardView AddScoreEvent;
    private String mto_iban;
    private String mfrom_iban;

    private TextView homeOddsText, drawOddsText, awayOddsText;
    private TextView homeOdds, drawOdds, awayOdds;
    private ImageView crestHome, crestAway;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures_info);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        homeTeam = (TextView) findViewById(R.id.homeTeam);
        versus = (TextView) findViewById(R.id.versus);
        awayTeam = (TextView) findViewById(R.id.awayTeam);
        homeScore = (EditText) findViewById(R.id.setHScore);
        awayScore = (EditText) findViewById(R.id.setAScore);
        setPrice = (EditText) findViewById(R.id.setPrice);
        homeScoreText = (TextView) findViewById(R.id.setHText);
        awayScoreText = (TextView) findViewById(R.id.setAText);
        priceText = (TextView) findViewById(R.id.setPriceText);
        priceText.setText("Amount:");
        AddScoreEvent = (CardView) findViewById(R.id.setScoresButton);

        homeOddsText = (TextView) findViewById(R.id.homeOddsText);
        drawOddsText = (TextView) findViewById(R.id.drawOddsText);
        awayOddsText = (TextView) findViewById(R.id.awayOddsText);
        homeOdds = (TextView) findViewById(R.id.homeOdds);
        drawOdds = (TextView) findViewById(R.id.drawOdds);
        awayOdds = (TextView) findViewById(R.id.awayOdds);

        crestHome = (ImageView) findViewById(R.id.crestHome);
        crestAway = (ImageView) findViewById(R.id.crestAway);


        mRootRef.child("Users").child(EnteredUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mfrom_iban = dataSnapshot.child("from_iban").getValue().toString();
                mto_iban = dataSnapshot.child("to_iban").getValue().toString();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast toast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Intent intent = getIntent();
        String homeTeamPerc = intent.getStringExtra("homeTeamPerc");
        String awayTeamPerc = intent.getStringExtra("awayTeamPerc");
        String drawPerc = intent.getStringExtra("drawPerc");

        JSONObject fixtureObject = null;
        try {
            fixtureObject = (JSONObject) fixturesArray.get(fixPos);

            String home_team_name = (String) fixtureObject.get("match_hometeam_name");
            String away_team_name = (String) fixtureObject.get("match_awayteam_name");
            if(home_team_name.matches("Brighton &amp; Hove Albion"))
            {
                home_team_name = "Brighton & Hove Albion";
            }else if (away_team_name.matches("Brighton &amp; Hove Albion"))
            {
                away_team_name = "Brighton & Hove Albion";
            }
            final String finalHome_team_name = home_team_name;
            final String finalAway_team_name = away_team_name;
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
            final String matchID = (String) fixtureObject.get("match_id");
            final String matchDate = (String) fixtureObject.get("match_date");
            final String matchTime = (String) fixtureObject.get("match_time");


            homeTeam.setText(home_team_name);
            versus.setText("V");
            awayTeam.setText(away_team_name);
            homeScoreText.setText(home_team_name + ":");
            awayScoreText.setText(away_team_name + ":");

            homeOddsText.setText(home_team_name);
            drawOddsText.setText("Draw");
            awayOddsText.setText(away_team_name);

            homeOdds.setText(homeTeamPerc +"%");
            drawOdds.setText(drawPerc +"%");
            awayOdds.setText(awayTeamPerc +"%");

            System.out.println(homeTeamPerc + "\n" + awayTeamPerc + "\n" + drawPerc + "\n-------------");
            //Toast toast = Toast.makeText(getApplicationContext(), "\""+EnteredUsername+"\"", Toast.LENGTH_LONG);
            //toast.show();
            if(home_team_name.matches("Brighton & Hove Albion"))
            {
                home_team_name = "Brighton &amp; Hove Albion";
            }else if (away_team_name.matches("Brighton & Hove Albion"))
            {
                away_team_name = "Brighton & Hove Albion";
            }
            final String finalHome_team_name2 = home_team_name;
            final String finalAway_team_name2 = away_team_name;
            AddScoreEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                    String HomeTeamScore = homeScore.getText().toString();
                    String AwayTeamScore = awayScore.getText().toString();
                    String amount = setPrice.getText().toString();


                    Firebase MatchIDChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("matchID");
                    MatchIDChild.setValue(matchID);
                    Firebase HomeTeamChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("HomeTeam");
                    HomeTeamChild.setValue(finalHome_team_name2);
                    Firebase AwayTeamChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("AwayTeam");
                    AwayTeamChild.setValue(finalAway_team_name2);
                    Firebase HomeScoreChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("HomeScoreGuess");
                    HomeScoreChild.setValue(HomeTeamScore);
                    Firebase AwayScoreChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("AwayScoreGuess");
                    AwayScoreChild.setValue(AwayTeamScore);
                    Firebase MatchDateChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("MatchDate");
                    MatchDateChild.setValue(matchDate);
                    Firebase MatchTimeChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("MatchTime");
                    MatchTimeChild.setValue(matchTime);
                    Firebase from_ibanChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("from_iban");
                    from_ibanChild.setValue(mfrom_iban);
                    Firebase to_ibanChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("to_iban");
                    to_ibanChild.setValue(mto_iban);
                    Firebase amountChild = mRootRef.child("Events").child(UserIdentifier).child(matchID).child("amount");
                    amountChild.setValue(amount);
                    finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(), "There was an error connecting", Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
