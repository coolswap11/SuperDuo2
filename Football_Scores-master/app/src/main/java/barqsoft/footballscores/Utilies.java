package barqsoft.footballscores;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int BUNDESLIGA = 351;
    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEAGUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMERA_LIGA = 402;
    public static final int Bundesliga3 = 403;
    public static final int EREDIVISIE = 404;
    public static final int CHAMP =  405;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";
            case BUNDESLIGA1 : return "Bundesliga";
            case BUNDESLIGA2 : return "Bundesliga";
            case LIGUE1 : return "Ligue 1 2015/16";
            case LIGUE2 : return "Ligue 2 2015/16";
            case PREMIER_LEAGUE : return "Premier League";
            case SEGUNDA_DIVISION : return "Segunda Division 2015/16";
            case PRIMERA_LIGA : return "Primeira Liga 2015/16";
            case Bundesliga3 : return "Bundesliga";
            case EREDIVISIE : return "Eredivisie 2015/16";
            case CHAMP : return "Champions League 2015/2016";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
//        System.out.println("team "+teamname);
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "FC Bayern München":return R.drawable.bayern_logo;
            case "Shakhtar Donetsk":return R.drawable.bayern_logo;
            case  "VfL Wolfsburg":return R.drawable.bayern_logo;
            case  "Bayer Leverkusen":return R.drawable.bayern_logo;
            case  "FC Barcelona":return R.drawable.barcelona;
            case  "KAA Gent":return R.drawable.gent;
            case  "FC Zenit St. Petersburg":return R.drawable.photo;
            case  "Chelsea FC":return R.drawable.chelsea;
            case  "FC Porto":return R.drawable.porto;
            case  "AS Roma":return R.drawable.roma;
            case  "FK BATE Baryssau":return R.drawable.bate;
            case  "Olympiacos F.C.":return R.drawable.olympiacos;
            case  "Arsenal FC":return R.drawable.arsenal;
            case  "GNK Dinamo Zagreb":return R.drawable.dinamozagreb;
            default: return R.drawable.no_icon;
        }
    }
}
