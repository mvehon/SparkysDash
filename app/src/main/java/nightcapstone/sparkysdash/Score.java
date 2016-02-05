package nightcapstone.sparkysdash;

/**
 * Created by Matthew on 2/4/2016.
 */
public class Score implements Comparable<Score>{
    public String name="xxx";
    public int score=0;

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
