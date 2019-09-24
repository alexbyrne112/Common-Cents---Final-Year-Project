package com.example.alex.commoncents;

public class FixtureMatch {

    //Object for Match fixtures in function fragment
    public String HomeTeam;
    public String AwayTeam;
    public String matchDate;
    public String matchTime;

    //Constructors for match Fixtures
    public FixtureMatch(String hteam, String ateam, String mDate, String mTime)
    {
        HomeTeam = hteam;
        AwayTeam = ateam;
        matchDate = mDate;
        matchTime = mTime;
    }


}
