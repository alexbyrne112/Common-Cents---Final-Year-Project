package com.example.alex.commoncents;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.alex.commoncents.RegisterActivity.vTo_iban;
import static com.example.alex.commoncents.RegisterActivity.vFrom_iban;

public class verificationActivity extends AppCompatActivity {

    //Firebase
    private Firebase mRootRef;
    //College IP & Home ip change
    //public static final String ibanURL = "http://213.233.132.142/bank_commoncents/v1/verify.php?iban=";
    public static final String ibanURL = "https://serene-beach-25172.herokuapp.com/v1/verify.php?iban=";
    public static String userID;

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
        setContentView(R.layout.activity_verification);
        mRootRef.setAndroidContext(this);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");

        currentAccText = (TextView) findViewById(R.id.currentPinText);
        currentAccText.setText("  "+vFrom_iban);
        savingsAccPin = (TextView) findViewById(R.id.savingsPinText);
        savingsAccPin.setText("  "+vTo_iban);

        progress = (ProgressBar) findViewById(R.id.Loading);
        progress.setVisibility(View.VISIBLE);

        enteredCurrentPin = (EditText) findViewById(R.id.currentPin);
        enteredSavingsPin = (EditText) findViewById(R.id.savingsPin);
        submit = (CardView) findViewById(R.id.submitButton);

        final RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest currentRequest = new StringRequest(Request.Method.GET, ibanURL+vFrom_iban,
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
                System.out.println(error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(currentRequest);


        // Request a string response from the provided URL.
        StringRequest savingsRequest = new StringRequest(Request.Method.GET, ibanURL+vTo_iban,
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
                System.out.println(error);
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
                //UsernameValue, FirstNameValue, DOBValue, EmailValue, PasswordValue, from_ibanValue, to_ibanValue
                //get values from the previous intent
                Intent intent = getIntent();
                String UsernameValue = intent.getStringExtra("UsernameValue");
                String FirstNameValue = intent.getStringExtra("FirstNameValue");
                String DOBValue = intent.getStringExtra("DOBValue");
                String EmailValue = intent.getStringExtra("EmailValue");
                String PasswordValue = intent.getStringExtra("PasswordValue");
                String from_ibanValue = intent.getStringExtra("from_ibanValue");
                String to_ibanValue = intent.getStringExtra("to_ibanValue");


                if(currentPinAttempt.matches("") || savingsPinAttempt.matches("")) {
                    Toast messagetoast = Toast.makeText(getApplicationContext(), "Please Enter Both Pins", Toast.LENGTH_LONG);
                    messagetoast.show();
                }else{
                    if(attempts > 0) {
                        if (currentPinAttempt.matches(reqCurrPin) && savingsPinAttempt.matches(reqSavePin)) {
                            Toast toast = Toast.makeText(getApplicationContext(), reqCurrPin + "  " + reqSavePin, Toast.LENGTH_LONG);
                            toast.show();
                            userID = mRootRef.push().getKey();
                            //set username
                            Firebase UsernameChild = mRootRef.child("Users").child(UsernameValue).child("Username");
                            UsernameChild.setValue(UsernameValue);
                            //set first name
                            Firebase FirstNameChild = mRootRef.child("Users").child(UsernameValue).child("FirstName");
                            FirstNameChild.setValue(FirstNameValue);
                            //set Birthday
                            Firebase BdayChild = mRootRef.child("Users").child(UsernameValue).child("Date of Birth");
                            BdayChild.setValue(DOBValue);
                            //set email value
                            Firebase EmailChild = mRootRef.child("Users").child(UsernameValue).child("Email");
                            EmailChild.setValue(EmailValue);

                            //set password value
                            Firebase PasswordChild = mRootRef.child("Users").child(UsernameValue).child("Password");
                            PasswordChild.setValue(PasswordValue);



                            Firebase from_ibanChild = mRootRef.child("Users").child(UsernameValue).child("from_iban");
                            from_ibanChild.setValue(from_ibanValue);
                            vFrom_iban = from_ibanValue;
                            Firebase to_ibanChild = mRootRef.child("Users").child(UsernameValue).child("to_iban");
                            to_ibanChild.setValue(to_ibanValue);
                            vTo_iban = to_ibanValue;
                            Firebase idChild = mRootRef.child("Users").child(UsernameValue).child("ID");
                            idChild.setValue(userID);
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
