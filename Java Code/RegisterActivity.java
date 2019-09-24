package com.example.alex.commoncents;

import android.content.Intent;
import android.graphics.Color;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {
    private EditText sUsername;
    private EditText sFirstName;
    private EditText sDay;
    private EditText sMonth;
    private EditText sYear;
    private EditText sEmail;
    private EditText sPassword;
    private EditText sConfirmPassword;
    private CardView mAddButton;
    private Firebase mRootRef;
    private EditText sFrom_iban;
    private EditText sTo_iban;
    //private DatabaseReference mRootRef;
    private TextView cSignIn;
    //public static String userID;

    public static String vTo_iban;
    public static String vFrom_iban;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRootRef.setAndroidContext(this);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");
        //mRootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://commoncents-190e4.firebaseio.com/");
        sUsername = (EditText) findViewById(R.id.Username);
        sFirstName = (EditText) findViewById(R.id.FirstName);
        sDay = (EditText) findViewById(R.id.Day);
        sMonth = (EditText) findViewById(R.id.Month);
        sYear = (EditText) findViewById(R.id.Year);
        sEmail = (EditText) findViewById(R.id.Email);
        sPassword = (EditText) findViewById(R.id.Password);
        sConfirmPassword = (EditText) findViewById(R.id.confrimPassword);
        sFrom_iban = (EditText) findViewById(R.id.from_iban);
        sTo_iban = (EditText) findViewById(R.id.to_iban);
        mAddButton = (CardView) findViewById(R.id.AddButton);
        cSignIn = (TextView) findViewById(R.id.haveAccount);

        cSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                String UsernameValue = sUsername.getText().toString();
                String FirstNameValue = sFirstName.getText().toString();
                String DayValue = sDay.getText().toString();
                String MonthValue = sMonth.getText().toString();
                String YearValue = sYear.getText().toString();
                String EmailValue = sEmail.getText().toString();
                String PasswordValue = sPassword.getText().toString();
                String ConfirmPasswordValue = sConfirmPassword.getText().toString();
                String from_ibanValue = sTo_iban.getText().toString();
                String to_ibanValue = sTo_iban.getText().toString();
                String emailRegEx = "[a-zA-z0-9._-]+@[a-z]+\\.[a-z]{2,}";
                String DOBValue = DayValue + "/" + MonthValue + "/" + YearValue;
                String DOBRegEx = "([0-2]+[0-9]|[3]+[0-1])[/]([0]+[0-9]|[1]+[0-2])[/](19|20)+([0-9]{2})";

                if(FirstNameValue.matches("")||DayValue.matches("")||
                        MonthValue.matches("")||YearValue.matches("")||
                        EmailValue.matches("")||PasswordValue.matches("")||
                        ConfirmPasswordValue.matches("")||from_ibanValue.matches("")||
                        to_ibanValue.matches(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Fill Out All Fields", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    mRootRef.child("Usernames").child(UsernameValue).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue(String.class);
                            String UsernameValue = sUsername.getText().toString();
                            String FirstNameValue = sFirstName.getText().toString();
                            String DayValue = sDay.getText().toString();
                            String MonthValue = sMonth.getText().toString();
                            String YearValue = sYear.getText().toString();
                            String EmailValue = sEmail.getText().toString();
                            String PasswordValue = sPassword.getText().toString();
                            String ConfirmPasswordValue = sConfirmPassword.getText().toString();
                            String from_ibanValue = sFrom_iban.getText().toString();
                            String to_ibanValue = sTo_iban.getText().toString();
                            String emailRegEx = "[a-zA-z0-9._-]+@[a-z]+\\.[a-z]{2,}";
                            String DOBValue = DayValue + "/" + MonthValue + "/" + YearValue;
                            String DOBRegEx = "([0-2]+[0-9]|[3]+[0-1])[/]([0]+[0-9]|[1]+[0-2])[/](19|20)+([0-9]{2})";


                            if (value == null) {
                                if(DOBValue.matches(DOBRegEx)) {
                                    if (EmailValue.matches(emailRegEx)) {
                                        if (ConfirmPasswordValue.equals(PasswordValue) && !PasswordValue.equals("")) {
                                            vFrom_iban = from_ibanValue;
                                            vTo_iban = to_ibanValue;
                                            openIbanVerification(UsernameValue, FirstNameValue, DOBValue, EmailValue, PasswordValue, from_ibanValue, to_ibanValue);
                                            finish();

                                        } else if (!ConfirmPasswordValue.equals(PasswordValue)) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Passwords do not Match", Toast.LENGTH_LONG);
                                            toast.show();
                                            sPassword.setText("");
                                            sConfirmPassword.setText("");
                                            sPassword.setBackgroundColor(Color.RED);
                                            sConfirmPassword.setBackgroundColor(Color.RED);

                                        } else if (PasswordValue.equals("") && ConfirmPasswordValue.equals("")) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Please Enter a Password", Toast.LENGTH_LONG);
                                            toast.show();
                                            sPassword.setText("");
                                            sConfirmPassword.setText("");
                                            sPassword.setBackgroundColor(Color.RED);
                                            sConfirmPassword.setBackgroundColor(Color.RED);
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Please Enter a Valid E-mail", Toast.LENGTH_LONG);
                                        toast.show();
                                        sPassword.setText("");
                                        sConfirmPassword.setText("");
                                        sConfirmPassword.setBackgroundColor(Color.RED);
                                        sEmail.setBackgroundColor(Color.RED);
                                    }
                                }else
                                {
                                    Toast toast = Toast.makeText(getApplicationContext(), "please Enter a valid Date of Birth", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT);
                                toast.show();
                                sUsername.setText("");
                                sPassword.setText("");
                                sConfirmPassword.setText("");
                                sPassword.setBackgroundColor(Color.RED);
                                sConfirmPassword.setBackgroundColor(Color.RED);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }
        });
    }

    public void openSignIn()
    {
        finish();
    }

    public void openIbanVerification(String UsernameValue, String FirstNameValue, String DOBValue, String EmailValue, String PasswordValue, String from_ibanValue, String to_ibanValue)
    {
        Intent ibanVerification = new Intent(this, verificationActivity.class);
        ibanVerification.putExtra("UsernameValue", UsernameValue);
        ibanVerification.putExtra("FirstNameValue", FirstNameValue);
        ibanVerification.putExtra("DOBValue", DOBValue);
        ibanVerification.putExtra("EmailValue", EmailValue);

        //Encrypt Password
        String encryptedPassword = "";
        try {
            encryptedPassword = AESEncryption.encrypt(PasswordValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ibanVerification.putExtra("PasswordValue", encryptedPassword);
        ibanVerification.putExtra("from_ibanValue", from_ibanValue);
        ibanVerification.putExtra("to_ibanValue", to_ibanValue);
        startActivity(ibanVerification);
    }
}
