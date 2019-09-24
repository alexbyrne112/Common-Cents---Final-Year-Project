package com.example.alex.commoncents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.alex.commoncents.MainActivity.EnteredUsername;


public class ProfileFragment extends Fragment {
    private TextView UserName;
    private EditText UsernameEdit, NameEdit, EmailEdit, currentAcc, savingsAcc, oldPW, newPW, newPW2;
    private CardView InfoButton, AccountButton, PasswordButton;
    private Firebase mRootRef;

    private String username;
    private String email;
    private String name;
    private String birthday;
    private String fromiban;
    private String toiban;
    private String password;
    private String identifier;

    public static String newUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View profile = inflater.inflate(R.layout.fragment_profile, container, false);

        mRootRef = new Firebase("https://commoncents-190e4.firebaseio.com/");

        UserName = (TextView) profile.findViewById(R.id.Username);
        UsernameEdit = (EditText) profile.findViewById(R.id.UsernameEdit);
        NameEdit = (EditText) profile.findViewById(R.id.NameEdit);
        EmailEdit = (EditText) profile.findViewById(R.id.EmailEdit);
        currentAcc = (EditText) profile.findViewById(R.id.cAccEdit);
        savingsAcc = (EditText) profile.findViewById(R.id.sAccEdit);
        InfoButton = (CardView) profile.findViewById(R.id.InfoButton);
        AccountButton = (CardView) profile.findViewById(R.id.AccountButton);
        PasswordButton = (CardView) profile.findViewById(R.id.PasswordButton);
        oldPW = (EditText) profile.findViewById(R.id.oldPasswordEdit);
        newPW = (EditText) profile.findViewById(R.id.newPasswordEdit);
        newPW2 = (EditText) profile.findViewById(R.id.newPasswordEdit2);

        mRootRef.child("Users").child(EnteredUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = (String) dataSnapshot.child("Username").getValue();
                email = (String) dataSnapshot.child("Email").getValue();
                name = (String) dataSnapshot.child("FirstName").getValue();
                birthday = (String) dataSnapshot.child("Date of Birth").getValue();
                fromiban = (String) dataSnapshot.child("from_iban").getValue();
                toiban = (String) dataSnapshot.child("to_iban").getValue();
                password = (String) dataSnapshot.child("Password").getValue();
                identifier = (String) dataSnapshot.child("ID").getValue();

                UserName.setText(username);
                UsernameEdit.setText(username);
                NameEdit.setText(name);
                EmailEdit.setText(email);
                currentAcc.setText(fromiban);
                savingsAcc.setText(toiban);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        InfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUsername = UsernameEdit.getText().toString();
                final String newName = NameEdit.getText().toString();
                final String newEmail = EmailEdit.getText().toString();

                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

                if(newUsername.matches("") || newName.matches("") || newEmail.matches(""))
                {
                    Toast toast = Toast.makeText(getContext(), "Please Fill Out Relevant Fields", Toast.LENGTH_LONG);
                    toast.show();
                    UsernameEdit.setBackgroundColor(Color.RED);
                    NameEdit.setBackgroundColor(Color.RED);
                    EmailEdit.setBackgroundColor(Color.RED);

                }else {
                    mRootRef.child("Users").child(newUsername).child("Username").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String UsernameValue = dataSnapshot.getValue(String.class);
                            if (UsernameValue == null || UsernameValue.matches(username)) {

                                LayoutInflater LI = LayoutInflater.from(getContext());
                                View prompt = LI.inflate(R.layout.info_change, null);
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                                alertdialog.setView(prompt);
                                TextView changePrompt = (TextView) prompt.findViewById(R.id.changer);
                                changePrompt.setText("Are You Sure You Want To Change Your Personal Information?");
                                alertdialog.setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                EnteredUsername = newUsername;

                                                Firebase UsernameChild = mRootRef.child("Users").child(newUsername).child("Username");
                                                UsernameChild.setValue(newUsername);
                                                //set first name
                                                Firebase FirstNameChild = mRootRef.child("Users").child(newUsername).child("FirstName");
                                                FirstNameChild.setValue(newName);
                                                //set Birthday
                                                Firebase BdayChild = mRootRef.child("Users").child(newUsername).child("Date of Birth");
                                                BdayChild.setValue(birthday);
                                                //set email value
                                                Firebase EmailChild = mRootRef.child("Users").child(newUsername).child("Email");
                                                EmailChild.setValue(newEmail);
                                                //set password value etc.......
                                                Firebase PasswordChild = mRootRef.child("Users").child(newUsername).child("Password");
                                                PasswordChild.setValue(password);
                                                Firebase from_ibanChild = mRootRef.child("Users").child(newUsername).child("from_iban");
                                                from_ibanChild.setValue(fromiban);
                                                Firebase to_ibanChild = mRootRef.child("Users").child(newUsername).child("to_iban");
                                                to_ibanChild.setValue(toiban);
                                                Firebase idChild = mRootRef.child("Users").child(newUsername).child("ID");
                                                idChild.setValue(identifier);

                                                if(!EnteredUsername.matches(username))
                                                {
                                                    mRootRef.child("Users").child(username).removeValue();
                                                }
                                                Toast toast = Toast.makeText(getContext(), "Personal Information Updated", Toast.LENGTH_LONG);
                                                toast.show();
                                                refresh();
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

                            }else{
                                Toast toast = Toast.makeText(getContext(), "Sorry This Username Already Exists", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast toast = Toast.makeText(getContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
            }
        });


        AccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

                final String newCurrent = currentAcc.getText().toString();
                final String newSavings = savingsAcc.getText().toString();
                if(newSavings.matches("") || newCurrent.matches(""))
                {
                    Toast toast = Toast.makeText(getContext(), "Please Fill Out Relevant Fields", Toast.LENGTH_LONG);
                    toast.show();
                    currentAcc.setBackgroundColor(Color.RED);
                    savingsAcc.setBackgroundColor(Color.RED);
                }else {

                    if(newCurrent.matches(fromiban) && newSavings.matches(toiban))
                    {
                        Toast toast = Toast.makeText(getContext(), "Accounts Are Up To Date", Toast.LENGTH_LONG);
                        toast.show();
                        currentAcc.setText(fromiban);
                        savingsAcc.setText(toiban);
                    }else{
                        LayoutInflater LI = LayoutInflater.from(getContext());
                        View prompt = LI.inflate(R.layout.info_change, null);
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                        alertdialog.setView(prompt);
                        TextView changePrompt = (TextView) prompt.findViewById(R.id.changer);
                        changePrompt.setText("Are You Sure You Want To Change Your Accounts?");
                        alertdialog.setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent ibanChangeVerification = new Intent(getContext(), Verification2.class);
                                        ibanChangeVerification.putExtra("Current Account", newCurrent);
                                        ibanChangeVerification.putExtra("Savings Account", newSavings);
                                        startActivity(ibanChangeVerification);

                                        refresh();
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
                }
            }
        });


        PasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));

                mRootRef.child("Users").child(EnteredUsername).child("Password").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String EnteredOldPassword = oldPW.getText().toString();
                        String EnteredNewPassword = newPW.getText().toString();
                        String EnteredNewPassword2 = newPW2.getText().toString();
                        String EncryptedPassword = dataSnapshot.getValue(String.class);
                        //Password Decryption
                        String enteredOldPasswordEncrypted = "";
                        try {
                            enteredOldPasswordEncrypted = AESEncryption.encrypt(EnteredOldPassword);//call encrypt function
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String encryptedPassword1 = "";
                        String encryptedPassword2 = "";
                        try {
                            encryptedPassword1 = AESEncryption.encrypt(EnteredNewPassword);
                            encryptedPassword2 = AESEncryption.encrypt(EnteredNewPassword2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if(enteredOldPasswordEncrypted.matches(EncryptedPassword))
                        {
                            if(encryptedPassword1.matches(encryptedPassword2))
                            {
                                LayoutInflater LI = LayoutInflater.from(getContext());
                                View prompt = LI.inflate(R.layout.info_change, null);
                                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                                alertdialog.setView(prompt);
                                TextView changePrompt = (TextView) prompt.findViewById(R.id.changer);
                                changePrompt.setText("Are You Sure You Want To Change Your Password?");
                                final String finalEncryptedPassword = encryptedPassword1;
                                alertdialog.setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Firebase PasswordChild = mRootRef.child("Users").child(EnteredUsername).child("Password");
                                                PasswordChild.setValue(finalEncryptedPassword);
                                                Toast toast = Toast.makeText(getContext(), "Password Change Successful", Toast.LENGTH_LONG);
                                                toast.show();
                                                refresh();
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
                            }else{
                                Toast toast = Toast.makeText(getContext(), "Passwords do not Match", Toast.LENGTH_LONG);
                                toast.show();
                                oldPW.setText("");
                                oldPW.setBackgroundColor(Color.RED);
                                newPW.setText("");
                                newPW.setBackgroundColor(Color.RED);
                                newPW2.setText("");
                                newPW2.setBackgroundColor(Color.RED);
                            }
                        }else{
                            Toast toast = Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_LONG);
                            toast.show();
                            oldPW.setText("");
                            oldPW.setBackgroundColor(Color.RED);
                            newPW.setText("");
                            newPW.setBackgroundColor(Color.RED);
                            newPW2.setText("");
                            newPW2.setBackgroundColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast toast = Toast.makeText(getContext(), "Sorry There Was an Error Connecting ", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });

        return profile;
    }
    private void refresh()
    {
        FragmentManager fragmentManager = getFragmentManager ();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
        fragmentTransaction.commit ();
    }
}
