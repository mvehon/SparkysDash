package nightcapstone.sparkysdash;

/**
 * Created by Matthew on 11/30/2015.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class OptionsDialog extends Dialog {
    Score sc;
    ArrayList<Score> scorelist = new ArrayList<Score>();
    SharedPreferences prefs = getContext().getSharedPreferences("com.nightcapstone.sparkysdash",
            Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();

    private Game game=null;
    private MainMenu mainMenu=null;
    private GameView gameView;

    Button ok_button;
    SeekBar music_slider, soundeffect_slider;
    TextView slider_txt1, slider_txt2;

    public static MediaPlayer musicPlayer;

    public OptionsDialog(Game game) {
        super(game);
        this.game = game;
        gameView = game.getGameView();
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        musicPlayer = game.musicPlayer;
        init();

    }
    public OptionsDialog(MainMenu mainMenu) {
        super(mainMenu);
        this.mainMenu = mainMenu;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        init();

    }

    public void init() {
        this.setContentView(R.layout.options);
        this.setCancelable(false);

        ok_button = (Button)findViewById(R.id.ok_button);

        music_slider = (SeekBar)findViewById(R.id.music_slider);
        soundeffect_slider = (SeekBar)findViewById(R.id.soundeffect_slider);

        music_slider.setProgress(prefs.getInt("music_volume",50));
        soundeffect_slider.setProgress(prefs.getInt("soundeffect_volume",50));

        slider_txt1 = (TextView)findViewById(R.id.slider_txt1);
        slider_txt2 = (TextView)findViewById(R.id.slider_txt2);

        slider_txt1.setText(Integer.toString(music_slider.getProgress()));
        slider_txt2.setText(Integer.toString(soundeffect_slider.getProgress()));


        music_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                slider_txt1.setText(Integer.toString(progress));
                if(game!=null){
                    musicPlayer.setVolume(progress*0.01f, progress*0.01f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        soundeffect_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                slider_txt2.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                prefs.edit().putInt("music_volume", music_slider.getProgress()).commit();
                prefs.edit().putInt("soundeffect_volume", soundeffect_slider.getProgress()).commit();
                if(game!=null){
                    gameView.resume();
                }
            }
        });

    }


}