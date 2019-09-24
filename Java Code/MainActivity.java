package com.example.alex.commoncents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private TextView cRegister;
    private CardView SignIn;
    private EditText vUsername;
    private EditText vPassword;
    private Firebase mRootRef;
    public static String EnteredUsername;
    public static String UserIdentifier;
    public ImageView logo;
    //private ProgressDialog progess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootRef.setAndroidContext(this);

        //old call
        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        //new call
        //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://commoncents-190e4.firebaseio.com/Usernames/");
        cRegister = (TextView) findViewById(R.id.Register);
        SignIn = (CardView) findViewById(R.id.signInButton);
        vUsername = (EditText) findViewById(R.id.Username);
        vPassword = (EditText) findViewById(R.id.Password);
        logo = (ImageView) findViewById(R.id.logo);
        Picasso.get().load(R.drawable.main_page_logo).into(logo);
        cRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                openRegister();
            }
        });


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnteredUsername = vUsername.getText().toString();

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

                //final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://commoncents-190e4.firebaseio.com/Usernames/"+EnteredUsername+"/");
                if(EnteredUsername.matches(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Enter a Username", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    mRootRef.child("Users").child(EnteredUsername).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String UsernameValue = dataSnapshot.getValue(String.class);
                            if (UsernameValue == null) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Username Does Not Exist", Toast.LENGTH_LONG);
                                toast.show();
                                vPassword.setText("");
                                vUsername.setText("");
                            } else {
                                mRootRef.child("Users").child(EnteredUsername).child("ID").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        UserIdentifier = dataSnapshot.getValue().toString();

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                                mRootRef.child("Users").child(EnteredUsername).child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String EnteredPassword = vPassword.getText().toString();
                                        String EncryptedPassword = dataSnapshot.getValue(String.class);

                                        //Password Decryption
                                        String EnteredPasswordEncrypt = "";
                                        try {
                                            EnteredPasswordEncrypt = AESEncryption.encrypt(EnteredPassword);//call decrypt function
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (EncryptedPassword.equals(EnteredPasswordEncrypt)) {
                                            openUserArea();
                                        } else {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG);
                                            toast.show();
                                            vPassword.setText("");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            }
        });

    }


    public void openUserArea()
    {
        Intent intentUserArea = new Intent(this, UserArea.class);
        startActivity(intentUserArea);
    }
    public void openRegister()
    {
        Intent intentRegister = new Intent(this, RegisterActivity.class);
        startActivity(intentRegister);
    }
}
