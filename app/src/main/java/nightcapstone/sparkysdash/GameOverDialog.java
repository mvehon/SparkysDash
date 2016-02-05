package nightcapstone.sparkysdash;

/**
 * Created by Matthew on 11/30/2015.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameOverDialog extends Dialog {
    private ArrayList<Score> scorelist;
    SharedPreferences prefs = getContext().getSharedPreferences("com.nightcapstone.sparkysdash",
            Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();


    public static final String score_save_name = "score_save";
    public static final String best_score_key = "score";

    private Game game;
    private GameView gameView;

    private TextView tvCurrentScoreVal;
    private TextView tvBestScoreVal;

    public GameOverDialog(Game game) {
        super(game);
        this.game = game;
        gameView = game.getGameView();
        this.setContentView(R.layout.gameover);
        this.setCancelable(false);

        tvCurrentScoreVal = (TextView) findViewById(R.id.tv_current_score_value);
        tvBestScoreVal = (TextView) findViewById(R.id.tv_best_score_value);
    }

    public void init() {
        Button okButton = (Button) findViewById(R.id.b_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                game.finish();
            }
        });

        manageScore();
    }

    private void manageScore() {
        try {
            scorelist = (ArrayList<Score>) InternalStorage.readObject(getContext(), "scorelist");
        } catch (IOException e1) {
            e1.printStackTrace();
            scorelist.add(new Score());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            scorelist.add(new Score());
        }
        Collections.sort(scorelist);

        int i=0;
        Boolean highscore = false;
        do{
            if(gameView.getPoints()>scorelist.get(i).score){
                highscore=true;
            }
        }while(i<10 && !highscore);

        if(highscore){
            //Make player input box visible

        }
        //Display the top 10 scores, inflate the rows into the tablelayout


        SharedPreferences saves = game.getSharedPreferences(score_save_name, 0);
        int oldPoints = saves.getInt(best_score_key, 0);
        if (gameView.getPoints() > oldPoints) {
            // Save new highscore
            SharedPreferences.Editor editor = saves.edit();
            editor.putInt(best_score_key, gameView.getPoints());
            tvBestScoreVal.setTextColor(Color.RED);
            editor.commit();
        }
        tvCurrentScoreVal.setText("" + gameView.getPoints());
        tvBestScoreVal.setText("" + oldPoints);
    }

}