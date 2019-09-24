package com.example.alex.commoncents;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alex.commoncents.MainActivity.EnteredUsername;
import static com.example.alex.commoncents.MainActivity.UserIdentifier;
import static com.example.alex.commoncents.RegisterActivity.vFrom_iban;
import static com.example.alex.commoncents.RegisterActivity.vTo_iban;

public class Verification2 extends AppCompatActivity {

    private Firebase mRootRef;
    //College IP & Home ip change
    //public static final String ibanURL = "http://213.233.132.142/bank_commoncents/v1/verify.php?iban=";
    public static final String ibanURL = "https://serene-beach-25172.herokuapp.com/v1/verify.php?iban=";
    public static String userID;

    private String currIban, saveIban;
    private TextView currentAccText;
    private TextView savingsAccPin;
    private EditText enteredCurrentPin;
    private EditText enteredSavingsPin;

    private CardView submit;
    private String reqCurrPin;
    private String reqSavePin;
    private int attempts = 4;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification2);
        mRootRef.setAndroidContext(this);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");

        Intent intent = getIntent();
        currIban = intent.getStringExtra("Current Account");
        saveIban = intent.getStringExtra("Savings Account");

        currentAccText = (TextView) findViewById(R.id.currentPinText);
        currentAccText.setText("  " +currIban);
        savingsAccPin = (TextView) findViewById(R.id.savingsPinText);
        savingsAccPin.setText("  "+ saveIban);

        progress = (ProgressBar) findViewById(R.id.Loading);
        progress.setVisibility(View.VISIBLE);

        enteredCurrentPin = (EditText) findViewById(R.id.currentPin);
        enteredSavingsPin = (EditText) findViewById(R.id.savingsPin);
        submit = (CardView) findViewById(R.id.submitButton);

        final RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest currentRequest = new StringRequest(Request.Method.GET, ibanURL+currIban,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject Object = new JSONObject(response);
                            JSONArray dataArray = Object.getJSONArray("data");
                            JSONObject pin = dataArray.getJSONObject(0);
                            String currentPin = pin.getString("user_pin");
                            reqCurrPin = currentPin;
                            progress.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.setVisibility(View.INVISIBLE);
                            Toast errortoast = Toast.makeText(getApplicationContext(), "There has Been an Error Connecting", Toast.LENGTH_LONG);
                            errortoast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "There has Been an Error Connecting", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(currentRequest);


        // Request a string response from the provided URL.
        StringRequest savingsRequest = new StringRequest(Request.Method.GET, ibanURL+saveIban,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject Object = new JSONObject(response);
                            JSONArray dataArray = Object.getJSONArray("data");
                            JSONObject pin = dataArray.getJSONObject(0);
                            String savingsPin = pin.getString("user_pin");
                            reqSavePin = savingsPin;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast errortoast = Toast.makeText(getApplicationContext(), "There has Been an Error Connecting", Toast.LENGTH_LONG);
                            errortoast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getApplicationContext(), "There has Been an Error Connecting", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(savingsRequest);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPinAttempt = enteredCurrentPin.getText().toString();
                String savingsPinAttempt = enteredSavingsPin.getText().toString();
                try {
                    currentPinAttempt = AESEncryption.encrypt(currentPinAttempt);
                    savingsPinAttempt = AESEncryption.encrypt(savingsPinAttempt);
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if(currentPinAttempt.matches("") || savingsPinAttempt.matches("")) {
                    Toast messagetoast = Toast.makeText(getApplicationContext(), "Please Enter Both Pins", Toast.LENGTH_LONG);
                    messagetoast.show();
                }else{
                    if(attempts > 0) {
                        if (currentPinAttempt.matches(reqCurrPin) && savingsPinAttempt.matches(reqSavePin)) {
                            Toast toast = Toast.makeText(getApplicationContext(), reqCurrPin + "  " + reqSavePin, Toast.LENGTH_LONG);
                            toast.show();
                            //change the Ibans in the Event table to the update ones
                            mRootRef.child("Events").child(UserIdentifier).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        final Firebase from_ibanChild = mRootRef.child("Users").child(EnteredUsername).child("from_iban");
                                        from_ibanChild.setValue(currIban);
                                        Firebase to_ibanChild = mRootRef.child("Users").child(EnteredUsername).child("to_iban");
                                        to_ibanChild.setValue(saveIban);

                                        String key = snapshot.getKey();
                                        Firebase from_ibanEventChild = mRootRef.child("Events").child(UserIdentifier).child(key).child("from_iban");
                                        from_ibanEventChild.setValue(currIban);
                                        Firebase to_ibanEventChild = mRootRef.child("Events").child(UserIdentifier).child(key).child("to_iban");
                                        to_ibanEventChild.setValue(saveIban);
                                        System.out.println(key + "--------------------------------------------------------");
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast toasty = Toast.makeText(getApplicationContext(), "Error changing Accounts", Toast.LENGTH_LONG);
                                    toasty.show();
                                }
                            });
                            Toast toasty = Toast.makeText(getApplicationContext(), "Account Change Successful", Toast.LENGTH_LONG);
                            toasty.show();
                            finish();

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Pin, " + attempts + " Attempts Remaining", Toast.LENGTH_LONG);
                            toast.show();
                            attempts--;
                            enteredCurrentPin.setText("");
                            enteredCurrentPin.setBackgroundColor(Color.RED);
                            enteredSavingsPin.setText("");
                            enteredSavingsPin.setBackgroundColor(Color.RED);
                        }
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "You Have No Attempts Remaining", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    }
                }
            }
        });

    }
}
