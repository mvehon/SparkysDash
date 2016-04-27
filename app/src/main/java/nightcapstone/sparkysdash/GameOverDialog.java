package nightcapstone.sparkysdash;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class GameOverDialog extends Dialog {
    Score sc;
    ArrayList<Score> scorelist = new ArrayList<Score>();
    SharedPreferences prefs = getContext().getSharedPreferences("com.nightcapstone.sparkysdash",
            Context.MODE_PRIVATE);

    private Game game;
    private GameView gameView;

    TableLayout playerscorebox;
    LinearLayout svc;
    LinearLayout playerinputbox;
    Button submitscore;
    EditText edit_name;
    TextView edit_score;
    Boolean highscore = false;

    public GameOverDialog(Game game) {
        super(game);
        this.game = game;
        gameView = game.getGameView();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.gameover);
        this.setCancelable(false);

        playerscorebox = (TableLayout) findViewById(R.id.playerscorebox);
        playerinputbox = (LinearLayout) findViewById(R.id.playerinputbox);
        submitscore = (Button) findViewById(R.id.submitscore);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_score = (TextView) findViewById(R.id.edit_score);
        svc = (LinearLayout) findViewById(R.id.svc);
    }

    public void init() {
        manageScore();

        submitscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (highscore) {
                    recordHighScore();
                } else {
                    dismiss();
                    game.finish();
                }
            }
        });

        //Clear on hitting 'Done', removed for now
/*
        edit_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(highscore){
                        recordHighScore();
                    } else {
                        dismiss();
                        game.finish();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        */
    }

    private void manageScore() {
        edit_score.setText(Integer.toString(gameView.getPoints()));
        try {
            scorelist = (ArrayList<Score>) InternalStorage.readObject(getContext(), "scorelist");
        } catch (IOException e1) {
            e1.printStackTrace();
            for (int x = 0; x < 10; x++) {
                sc = new Score();
                scorelist.add(sc);
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            for (int x = 0; x < 10; x++) {
                sc = new Score();
                scorelist.add(sc);
            }
        }
        Collections.sort(scorelist, Collections.reverseOrder());

        int i = 0;

        do {
            if (gameView.getPoints() > scorelist.get(i).score) {
                highscore = true;
            }
            i++;
        } while (i < 10 && !highscore);

        if (highscore) {
            //Make player input box visible
            edit_name.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "New highscore!", Toast.LENGTH_LONG).show();
        }


        //Display the top 10 scores, inflate the rows into the tablelayout
        LayoutInflater inflater;
        try {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (NullPointerException p) {
            return;
        }
        for (int j = 0; j < 10; j++) {
            svc.addView(inflater.inflate(R.layout.gameoverrow, null));
            TableRow tr = (TableRow) playerscorebox.findViewById(R.id.tr);
            TextView row_name = (TextView) tr.findViewById(R.id.row_name);
            TextView row_score = (TextView) tr.findViewById(R.id.row_score);
            tr.setId(j);
            row_name.setText(scorelist.get(j).getName());
            row_score.setText(Integer.toString(scorelist.get(j).getScore()));
        }

    }

    public void recordHighScore(){
        if (edit_name.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Score newscore = new Score(edit_name.getText().toString().toUpperCase(), gameView.getPoints());
                scorelist.add(newscore);
                Collections.sort(scorelist, Collections.reverseOrder());
                InternalStorage.writeObject(getContext(), "scorelist", scorelist);
            } catch (IOException e) {
                Log.e("ERR", e.getMessage());
            }
            dismiss();
            game.finish();
        }
    }

}