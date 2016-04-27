package nightcapstone.sparkysdash;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{
    public String name;
    public int score;

    public Score(){
        name = "XXX";
        score = 0;
    }
    public Score(String n, int s){
        name = n;
        score = s;
    }

    public int getScore(){
        return score;
    }
    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public void setScore(int s){
        score = s;
    }

    public int compareTo(Score other){
        return this.score - other.score;
    }

}
