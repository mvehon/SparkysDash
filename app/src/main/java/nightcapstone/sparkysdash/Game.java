package nightcapstone.sparkysdash;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


//TODO finish commenting this activity
public class Game extends Activity {
    GameView view;
    private boolean backPressed = false;
    GameOverDialog gameOverDialog;
    public MyHandler handler;
    SharedPreferences prefs;
    int temp;


    public static MediaPlayer musicPlayer;
    public boolean musicShouldPlay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GameView(this);
        gameOverDialog = new GameOverDialog(this);
        handler = new MyHandler(this);
        setContentView(view);
        musicPlayer = null;
        initMusicPlayer();
    }

    @Override
    public void onBackPressed() {
        if (backPressed) {
            super.onBackPressed();
        } else {
            backPressed = true;
            Toast.makeText(this, getResources().getString(R.string.on_back_press), Toast.LENGTH_LONG).show();
        }
    }

    GameView getGameView() {
        return view;
    }

    public void gameOver() {
        handler.sendMessage(Message.obtain(handler, MyHandler.GAME_OVER_DIALOG));

    }

    static class MyHandler extends Handler {
        public static final int GAME_OVER_DIALOG = 0;
        public static final int SHOW_TOAST = 1;

        private Game game;

        public MyHandler(Game game) {
            this.game = game;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GAME_OVER_DIALOG:
                    showGameOverDialog();
                    break;
                case SHOW_TOAST:
                    Toast.makeText(game, msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        private void showGameOverDialog() {
            //++Game.gameOverCounter;
            game.gameOverDialog.init();
            game.gameOverDialog.show();
        }
    }

    @Override
    protected void onPause() {
        view.pause();
        if(musicPlayer.isPlaying()){
            musicPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.drawOnce();
        musicPlayer.start();

    }


    public void initMusicPlayer(){
        prefs = this.getSharedPreferences("com.nightcapstone.sparkysdash",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        temp = prefs.getInt("music_volume", 50);
        float music_volume = temp * 0.01f;
        if(musicPlayer == null){
            // to avoid unnecessary reinitialisation
            musicPlayer = MediaPlayer.create(this, R.raw.wagon_wheel);
            musicPlayer.setLooping(true);
            musicPlayer.setVolume(music_volume, music_volume);
        }
        musicPlayer.seekTo(0);	// Reset song to position 0
        musicPlayer.start();
    }

}
