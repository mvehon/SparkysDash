package nightcapstone.sparkysdash;

/**
 * Created by Matthew on 11/30/2015.
 */

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverDialog extends Dialog {
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

