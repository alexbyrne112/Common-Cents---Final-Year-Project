package com.example.alex.commoncents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.VIBRATOR_SERVICE;

public class ResultsFragment extends Fragment {
    public static ListView fetchedResults;
    public static String two_weekback;
    public static String yesterday;
    public static int resPos;
    //Context context = getActivity();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View results = inflater.inflate(R.layout.fragment_results, container, false);

        fetchedResults = (ListView) results.findViewById(R.id.resultList);
        //code to get a date for days previous
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -14);
        Date weeks = calendar.getTime();
        two_weekback = weeks.toString();
        two_weekback = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(weeks);
        //Code to get yesterdays date so  the matches later done come up in results
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_YEAR, -1);
        Date weeks1 = calendar2.getTime();
        yesterday = weeks1.toString();
        yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(weeks1);


        //fetch result information from API
        fetchResults getResultData = new fetchResults();
        getResultData.execute(getContext());

        fetchedResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                resPos = position;
                openResultInfo();
            }
        });

        //return results fragment
        return results;
    }

    public void openResultInfo()
    {
        //new intent of match facts in result info
        Intent intentResultInfo = new Intent(getContext(), ResultsInfo.class);
        startActivity(intentResultInfo);
    }
}
