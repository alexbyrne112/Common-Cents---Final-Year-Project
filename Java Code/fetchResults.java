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
import java.util.ArrayList;

import static com.example.alex.commoncents.ResultsFragment.two_weekback;
import static com.example.alex.commoncents.ResultsFragment.yesterday;

public class fetchResults extends AsyncTask<Context, Void, Void> {
    public static JSONArray resultsArray;//public JSON array to be used in other activities
    public static String Hteam;
    public static String Ateam;
    public static String Hscore;
    public static String Ascore;
    public static String rDate;
    Context ctx;
    ResultMatch rm;
    ArrayList<ResultMatch> results = new ArrayList<ResultMatch>();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Context... params) {
        ctx = (Context) params[0];//passing in the context for the custom fixture row
        try {
            //try catch for URL requesting Football fixtures
            URL resulturl = new URL("https://apifootball.com/api/?action=get_events&from="+two_weekback+"&to="+yesterday+"&league_id=62&APIkey=894774b4eac49f8b7a14425609d7d736048e81188d446a3fbf47630a5c906245");
            String link2 = resulturl.toString();
            System.out.println(link2);
            //opens URL connection
            HttpURLConnection urlconnect = (HttpURLConnection) resulturl.openConnection();
            InputStream inputStream = urlconnect.getInputStream();
            //puts the input stream into a buffer that can be read later
            BufferedReader resultReader = new BufferedReader(new InputStreamReader(inputStream));
            String resultData = "";
            StringBuilder resultData2 = new StringBuilder();

            while((resultData = resultReader.readLine()) != null)
            {
                resultData2.append(resultData).append('\n');
            }
            resultData = resultData2.toString();
            resultsArray = new JSONArray(resultData);
            for(int i = resultsArray.length() -1; i > -1;i --)
            {
                //JSON object with fixture data inside
                JSONObject resultsObject = (JSONObject) resultsArray.get(i);
                //set the string equaal to whats in the Json object.
                Hteam = (String) resultsObject.get("match_hometeam_name");
                Ateam = (String) resultsObject.get("match_awayteam_name");
                rDate = (String) resultsObject.get("match_date");
                Hscore = (String) resultsObject.get("match_hometeam_score");
                Ascore = (String) resultsObject.get("match_awayteam_score");
                if(Hteam.matches("Brighton &amp; Hove Albion"))
                {
                    Hteam = "Brighton & Hove Albion";
                }else if (Ateam.matches("Brighton &amp; Hove Albion"))
                {
                    Ateam = "Brighton & Hove Albion";
                }
                //set the String array for the custom adapter
                rm = new ResultMatch(Hteam, Ateam, rDate, Hscore, Ascore);
                //add object to array of objects
                results.add(rm);
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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //set fetched data text area to data received from the API
        ListAdapter resultAdapter = new resultsAdapter(ctx, results);
        ResultsFragment.fetchedResults.setAdapter(resultAdapter);
    }
}
