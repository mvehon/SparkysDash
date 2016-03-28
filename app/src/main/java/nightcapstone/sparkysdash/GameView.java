package nightcapstone.sparkysdash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//The gameview contains the canvas that the game appears on, as well as touch events
public class GameView extends SurfaceView {
    public static final long UPDATE_INTERVAL = 50;          //This controls the refresh rate of the game (FPS)
    private Timer timer = new Timer();                      //Set the timer for the threads to run
    private TimerTask timerTask;
    private SurfaceHolder holder;                           //The drawing surface
    private Game game;                                      //Instance of the game activity
    private Sparky player;                                  //The player character
    private Background background;                          //The scrolling background
    private List<Obstacle> obstacles = new ArrayList<Obstacle>();   //List of all obstacles
    private OptionButton optionButton;                      //The settings button that appears in game
    volatile private boolean paused = true;                 //Whether the game is in a pause state or not
    private Tutorial tutorial;                              //The tutorial that pops up before you start playing
    private boolean tutorialIsShown = true;                 //Whether it is showing
    private int points = 0;                                 //The point counter for passing obstacles
    long lastpressed=0;
    OptionsDialog optionsDialog;
    SharedPreferences prefs;

    public GameView(Context context) {
        super(context);
        this.game = (Game) context;
        setFocusable(true);

        //Initialize the game objects
        holder = getHolder();
        background = new Background(this, game);
        player = new Sparky(this, game);
        optionButton = new OptionButton(this, game);
        tutorial = new Tutorial(this, game);
    }

    //Start the thread timer
    private void startTimer() {
        setUpTimerTask();
        timer = new Timer();
        timer.schedule(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
    }

    //Stop the thread timer
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    //Hold and run the thread
    private void setUpTimerTask() {
        stopTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                GameView.this.run();
            }
        };
    }

    //When the user touches the screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        //If the player is not dead, the game is currently going on
        if (event.getAction() == MotionEvent.ACTION_DOWN && !this.player.isDead()) {
            //Close the tutorial if it is open
            if (tutorialIsShown) {
                tutorialIsShown = false;
                resume();
            }
            //If the option button was pressed
            else if (optionButton.isTouching((int) event.getX(), (int) event.getY()) && !this.paused) {
                optionMenuOpen();
            }
            //If it is a valid in game touch
            else {
                //If the touch was on the left side of the screen, perform a jump
                if (event.getX() <= this.getWidth() / 2) {
                    Log.d("Touch", "Jump");
                    lastpressed = System.currentTimeMillis();
                }
                //If the event was on the right hand of the screen, perform a slide
                else {
                    Log.d("Touch", "Slide");
                    this.player.Slide();
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP && !this.player.isDead() && event.getX() <= this.getWidth() / 2 && lastpressed!=0) {
            long touchduration = System.currentTimeMillis() - lastpressed;
            Log.d("Touch duration", Long.toString(touchduration));
            if (player.isTouchingGround()) {
                if (touchduration < 150) {
                    this.player.Jump();
                } else {
                    this.player.HighJump();
                }
            }
        }
        return true;
    }

    //Check all the things
    public void run() {
        checkPasses();
        checkOutOfRange();
        checkCollision();
        createObstacle();
        move();
        draw();
    }

    //Display the tutorial
    public void showTutorial() {
        player.move();
        optionButton.move();
        //Wait until the surface view exists
        while (!holder.getSurface().isValid()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Canvas canvas = holder.lockCanvas();
        drawCanvas(canvas, true);
        tutorial.move();
        tutorial.draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    //Controls the option menu
    public void optionMenuOpen() {
        stopTimer();
        paused = true;
        optionsDialog = new OptionsDialog(game);
        optionsDialog.init();
        optionsDialog.show();
    }

    //Handles the initial draw after a pause/starting the game
    public void drawOnce() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                if (tutorialIsShown) {
                    showTutorial();
                } else {
                    draw();
                }
            }
        })).start();
    }

    //Stops the game threads
    public void pause() {
        stopTimer();
        paused = true;
    }

    //Starts the game threads
    public void resume() {
        paused = false;
        startTimer();
    }

    //Updates the canvas
    private void draw() {
        while (!holder.getSurface().isValid()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Canvas canvas = holder.lockCanvas();
        drawCanvas(canvas, true);
        holder.unlockCanvasAndPost(canvas);
    }

    //Draws the objects and score on the canvas
    private void drawCanvas(Canvas canvas, boolean drawPlayer) {
        background.draw(canvas);
        for (Obstacle r : obstacles) {
            r.draw(canvas);
        }
        if (drawPlayer) {
            player.draw(canvas);
        }
        optionButton.draw(canvas);

        //Draws the score text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(120.f);
        canvas.drawText(Integer.toString(points), canvas.getWidth() / 2 - 30, 100, paint);
    }

    //If the player dies, make them fall in slow motion to the ground
    private void playerDeadFall() {
        player.dead();
        do {
            player.move();
            draw();
            // sleep
            try {
                Thread.sleep(UPDATE_INTERVAL / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!player.isTouchingGround());
    }

    //Check to see if the player passed an obstacle
    private void checkPasses() {
        for (Obstacle o : obstacles) {
            if (o.isPassed()) {
                if (!o.isAlreadyPassed) {
                    o.onPass();
                }
            }
        }
    }

    //Check to see if the obstacle has exited the screen, and remove it from the array
    private void checkOutOfRange() {
        for (int i = 0; i < obstacles.size(); i++) {
            if (this.obstacles.get(i).isOutOfRange()) {
                this.obstacles.remove(i);
                i--;
            }
        }
    }

    //Check if any two sprites are overlapping
    private void checkCollision() {
        for (Obstacle o : obstacles) {
            if (o.isColliding(player)) {
                o.onCollision();
                gameOver();
            }
        }
    }

    //Create new obstacle
    private void createObstacle() {
        if (obstacles.size() < 1) {
            obstacles.add(new Obstacle(this, game));
        }
    }

    //Update the positions of objects
    private void move() {
        for (Obstacle o : obstacles) {
            o.setSpeedX(-getSpeedX());
            o.move();
        }

        background.setSpeedX(-getSpeedX() / 2);
        background.move();
        optionButton.move();
        player.move();
    }


    //Get the speed of the game movement
    public int getSpeedX() {
        // 16 @ 720x1280 px
        int speedDefault = this.getWidth() / 45;
        return speedDefault;
    }

    //If the player dies, start the gameOver process
    public void gameOver() {
        deathSound();
        pause();
        playerDeadFall();
        game.gameOver();
    }

    //Get the player character
    public Sparky getPlayer() {
        return this.player;
    }

    //Get the game object
    public Game getGame() {
        return this.game;
    }

    //Increment the point total
    public void increasePoints() {
        points++;
    }

    //Get the current point total
    public int getPoints() {
        return points;
    }

    public void deathSound() {
        MediaPlayer musicPlayer = game.musicPlayer;
        musicPlayer.stop();

        prefs = getContext().getSharedPreferences("com.nightcapstone.sparkysdash",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int temp = prefs.getInt("soundeffect_volume", 50);
        float soundeffect_volume = temp * 0.01f;
        Log.d("temp", Integer.toString(temp));
        Log.d("music_volume", Float.toString(soundeffect_volume));

        musicPlayer = MediaPlayer.create(getContext(), R.raw.needlescratch);
        musicPlayer.setLooping(false);
        musicPlayer.setVolume(soundeffect_volume, soundeffect_volume);
        musicPlayer.seekTo(0);    // Reset song to position 0
        musicPlayer.start();
    }

}
