package com.example.alex.commoncents;


//Adapter class for event displayed in home
public class Event {
    public String HomeTeam;
    public String AwayTeam;
    public String HomeScoreGuess;
    public String AwayScoreGuess;
    public String MatchDate;
    public String MatchTime;
    public String matchID;

    public Event() {
    }

    public Event(String HomeTeam, String AwayTeam, String HomeScoreGuess, String AwayScoreGuess, String MatchDate, String MatchTime, String matchID){
        this.HomeTeam = HomeTeam;
        this.AwayTeam = AwayTeam;
        this.HomeScoreGuess = HomeScoreGuess;
        this.AwayScoreGuess = AwayScoreGuess;
        this.MatchDate = MatchDate;
        this.MatchDate = MatchTime;
        this.matchID = matchID;
    }

    //getters and setteres for the pribate fields in this adapter
    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    public void setHomeScoreGuess(String homeScoreGuess) {
        HomeScoreGuess = homeScoreGuess;
    }

    public void setAwayScoreGuess(String awayScoreGuess) {
        AwayScoreGuess = awayScoreGuess;
    }

    public void setMatchDate(String matchDate) {
        MatchDate = matchDate;
    }

    public void setMatchTime(String matchTime) {
        MatchTime = matchTime;
    }

    public void setMatchID(String MatchID) {
        matchID = MatchID;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }

    public String getAwayTeam() {
        return AwayTeam;
    }

    public String getHomeScoreGuess() {
        return HomeScoreGuess;
    }

    public String getAwayScoreGuess() {
        return AwayScoreGuess;
    }

    public String getMatchDate() {
        return MatchDate;
    }

    public String getMatchTime() {
        return MatchTime;
    }

    public String getMatchID() {
        return matchID;
    }
}
