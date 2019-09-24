package com.example.alex.commoncents;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.alex.commoncents.FunctionFragment.weekLater;

public class fetchLeagues extends AsyncTask<Context, Void, Void> {
    String data;
    public static JSONArray fixturesArray;//public JSON array to be used in other activities
    public static String Hteam;
    public static String Ateam;
    public static String fixdate;
    public static String fixtime;
    public  ProgressDialog progress;
    Context ctx;
    FixtureMatch fm;
    ArrayList<FixtureMatch> fixtures = new ArrayList<FixtureMatch>();

    String todaysDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    public fetchLeagues(Context context){
        //progress = new ProgressDialog(context);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Context... params) {
        ctx = (Context) params[0];//passing in the context for the custom fixture row
        try {
            //try catch for URL requesting Football fixtures
            URL leagueurl = new URL("https://apifootball.com/api/?action=get_events&from="+todaysDate+"&to="+weekLater+"&league_id=62&APIkey=894774b4eac49f8b7a14425609d7d736048e81188d446a3fbf47630a5c906245");
            String link = leagueurl.toString();
            System.out.println(link);
            //opens URL connection
            HttpURLConnection urlconnect = (HttpURLConnection) leagueurl.openConnection();
            InputStream inputStream = urlconnect.getInputStream();
            //puts the input stream into a buffer that can be read later
            BufferedReader leagueReader = new BufferedReader(new InputStreamReader(inputStream));
            String leagueData = "";
            StringBuilder data2 = new StringBuilder();

            while((leagueData = leagueReader.readLine()) != null)
            {
                data2.append(leagueData).append('\n');
            }
            data = data2.toString();
            fixturesArray = new JSONArray(data);
            for(int i = 0; i < fixturesArray.length();i ++)
            {
                //JSON object with fixture data inside
                JSONObject leagueObject = (JSONObject) fixturesArray.get(i);
                //set the string equal to whats in the Json object.
                Hteam = (String) leagueObject.get("match_hometeam_name");
                Ateam = (String) leagueObject.get("match_awayteam_name");
                fixdate = (String) leagueObject.get("match_date");
                fixtime = (String) leagueObject.get("match_time");
                if(Hteam.matches("Brighton &amp; Hove Albion"))
                {
                    Hteam = "Brighton & Hove Albion";
                }else if (Ateam.matches("Brighton &amp; Hove Albion"))
                {
                    Ateam = "Brighton & Hove Albion";
                }
                //set the String array for the custom adapter
                fm = new FixtureMatch(Hteam, Ateam, fixdate, fixtime);
                fixtures.add(fm);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        /*
        super.onPreExecute();
        progress.setMessage("Loading Fixtures");
        progress.show();*/
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //set fetched data text area to data received from the API
        ListAdapter fixtureAdapter = new fixturesAdapter(ctx, fixtures);
        FunctionFragment.fetchedFixtures.setAdapter(fixtureAdapter);
    }
}