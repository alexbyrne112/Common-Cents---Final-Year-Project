package com.example.alex.commoncents;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class UserArea extends AppCompatActivity {
    //public for animations
    public FragmentManager fm = getSupportFragmentManager();
    public FragmentTransaction ft;
    private int curr = 1;
    private int next;
    public static Activity UArefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //TO be able to refrsh lists
        UArefresh = this;

        ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            next = 1;
                            break;
                        case R.id.nav_function:
                            selectedFragment = new FunctionFragment();
                            next = 2;
                            break;
                        case R.id.nav_results:
                            selectedFragment = new ResultsFragment();
                            next = 3;
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            next = 4;
                            break;
                    }
                    if(curr < next){
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).replace(R.id.fragment_container, selectedFragment).commit();
                        curr = next;
                        return true;
                    }
                    else if(curr == next && next == 4)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        return true;
                    }
                    else if(curr == next && next == 1)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        return true;
                    }
                    else{
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left,R.anim.exit_right).replace(R.id.fragment_container, selectedFragment).commit();
                        curr = next;
                        return true;
                    }
                }
            };
}
