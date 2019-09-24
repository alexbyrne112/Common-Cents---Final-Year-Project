package com.example.alex.commoncents;

public class ResultMatch {

    //Object for Match fixtures in function fragment
    public String HomeTeam;
    public String AwayTeam;
    public String rDate;
    public String homeScore;
    public String awayScore;

    //Constructors for match Fixtures
    public ResultMatch(String hteam, String ateam, String resultDate,String hscore, String ascore)
    {
        HomeTeam = hteam;
        AwayTeam = ateam;
        rDate = resultDate;
        homeScore = hscore;
        awayScore = ascore;
    }
}
