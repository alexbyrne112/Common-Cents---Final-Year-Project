import pandas as pd
import numpy as np
import json
from scipy.stats import poisson
import statsmodels.api as sm
import statsmodels.formula.api as smf
import firebase_admin
from firebase_admin import firestore
from firebase import firebase

firebase = firebase.FirebaseApplication('https://commoncents-190e4.firebaseio.com')

#dataset = pd.read_csv("http://www.football-data.co.uk/mmz4281/1819/E1.csv")
dataset = pd.read_csv("http://www.football-data.co.uk/mmz4281/1819/E0.csv")
#dataset = pd.read_csv('data.csv', index_col=0)
dataset = dataset[['HomeTeam','AwayTeam','FTHG','FTAG']]
dataset = dataset.rename(columns={'FTHG': 'HomeGoals', 'FTAG': 'AwayGoals'})
mean = dataset.mean()
print(mean)

#BPL Teams
Teams = ["Man City", "Liverpool", "Tottenham", "Man United", "Arsenal", "Chelsea",
         "Wolves", "Watford", "Everton", "West Ham", "Bournemouth", "Leicester",
         "Crystal Palace", "Brighton", "Burnley", "Newcastle", "Cardiff",
         "Southampton", "Fulham", "Huddersfield"]
#Teams = ["Norwich", "Sheffield United", "Leeds", "West Brom", "Middlesbrough", "Bristol City",
#         "Derby", "Birmingham", "Nott'm Forest", "Preston", "Hull", "Sheffield Weds",
#        "Aston Villa", "Swansea", "Blackburn", "Brentford", "QPR",
#         "Stoke", "Wigan", "Millwall", "Reading", "Rotherham", "Bolton", "Ipswich"]

DS1 = dataset[['HomeTeam','AwayTeam','HomeGoals']].assign(home=1).rename(columns={'HomeTeam':'team', 'AwayTeam':'opponent','HomeGoals':'goals'})
DS2 = dataset[['AwayTeam','HomeTeam','AwayGoals']].assign(home=0).rename(columns={'AwayTeam':'team', 'HomeTeam':'opponent','AwayGoals':'goals'})

dataModel = pd.concat([DS1,DS2])


formula = "goals ~ home + team + opponent"
poissonModel = smf.glm(formula=formula, data=dataModel, family=sm.families.Poisson()).fit()



def simulate_match(foot_model, homeTeam, awayTeam, maxGoals=10):

    homeGoalsAVG = foot_model.predict(pd.DataFrame(data={'team': homeTeam, 'opponent': awayTeam,'home':1}, index=[1])).values[0]
    awayGoalsAVG = foot_model.predict(pd.DataFrame(data={'team': awayTeam, 'opponent': homeTeam,'home':0}, index=[1])).values[0]

    predictions = [[poisson.pmf(i, team_avg) for i in range(0, maxGoals+1)] for team_avg in [homeGoalsAVG, awayGoalsAVG]]
    return(np.outer(np.array(predictions[0]), np.array(predictions[1])))

data = {}
data['predictions'] = []

i = 0
j = 0
while i < len(Teams):
    while j < len(Teams):
        if i == 19 and j== 19:
            break
        elif j == i:
            j+= 1
        HomeTeam = Teams[i]
        AwayTeam = Teams[j]
        prediction = simulate_match(poissonModel, HomeTeam, AwayTeam, maxGoals=10)
        HomeW = np.sum(np.tril(prediction, -1))
        Draw = np.sum(np.diag(prediction))
        AwayW = np.sum(np.triu(prediction, 1))
        if(HomeTeam == "Tottenham"):
            HomeTeam = "Tottenham Hotspur"
        elif(HomeTeam == "Man United"):
            HomeTeam = "Manchester United"
        elif (HomeTeam == "Man City"):
            HomeTeam = "Manchester City"
        elif (HomeTeam == "Wolves"):
            HomeTeam = "Wolverhampton Wanderers"
        elif (HomeTeam == "West Ham"):
            HomeTeam = "West Ham United"
        elif (HomeTeam == "Bournemouth"):
            HomeTeam = "AFC Bournemouth"
        elif (HomeTeam == "Cardiff"):
            HomeTeam = "Cardiff City"
        elif (HomeTeam == "Brighton"):
            HomeTeam = "Brighton & Hove Albion"
        elif (HomeTeam == "Huddersfield"):
            HomeTeam = "Huddersfield Town"
        elif (HomeTeam == "Leicester"):
            HomeTeam = "Leicester City"
        elif (HomeTeam == "Newcastle"):
            HomeTeam = "Newcastle United"

        if (AwayTeam == "Tottenham"):
            AwayTeam = "Tottenham Hotspur"
        elif (AwayTeam == "Man United"):
            AwayTeam = "Manchester United"
        elif (AwayTeam == "Man City"):
            AwayTeam = "Manchester City"
        elif (AwayTeam == "Wolves"):
            AwayTeam = "Wolverhampton Wanderers"
        elif (AwayTeam == "West Ham"):
            AwayTeam = "West Ham United"
        elif (AwayTeam == "Bournemouth"):
            AwayTeam = "AFC Bournemouth"
        elif (AwayTeam == "Cardiff"):
            AwayTeam = "Cardiff City"
        elif (AwayTeam == "Brighton"):
            AwayTeam = "Brighton & Hove Albion"
        elif (AwayTeam == "Huddersfield"):
            AwayTeam = "Huddersfield Town"
        elif (AwayTeam == "Leicester"):
            AwayTeam = "Leicester City"
        elif (AwayTeam == "Newcastle"):
            AwayTeam = "Newcastle United"


        data['predictions'].append({
            'HomeTeam' : HomeTeam,
            'HomeWin' : HomeW,
            'AwayTeam' : AwayTeam,
            'AwayWin' : AwayW,
            'Draw' : Draw
        })
        print(HomeTeam, ' Win: ', HomeW * 100)
        print('Draw: ', Draw * 100)
        print(AwayTeam, ' Win:', AwayW * 100)
        print("------------------")
        j+=1
    i+=1
    j=0

#result = firebase.post('/Predictions', data)
firebase.put('https://commoncents-190e4.firebaseio.com','Predictions',data)
with open('predictions.json', 'w') as outfile:
    json.dump(data, outfile)