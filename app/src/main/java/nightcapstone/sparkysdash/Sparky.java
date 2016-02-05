package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Sparky extends Sprite {
    public static Bitmap globalBitmap;
    protected boolean isDead = false;

    public Sparky(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            //globalBitmap = getScaledBitmapAlpha8(game, R.drawable.sparkysprite);
            globalBitmap = getScaledBitmapAlpha8(game, R.drawable.sparkysheet);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 6);                                      // 8 frames in a row
        this.height = this.bitmap.getHeight()/5;                                              // 4 frames in a column
        this.frameTime = 1;                                                                     // the frame will change every 3 runs
        //this.y = game.getResources().getDisplayMetrics().heightPixels / 2 - this.height/2;
        this.y = (int) (game.getResources().getDisplayMetrics().heightPixels - game.getResources().getDisplayMetrics().heightPixels *
                Background.GROUND_HEIGHT - this.height * 1.1);                              // Start level with the ground
    }

    @Override
    public void move() {
        super.move();
        this.x = this.view.getWidth() / 6;
        if (speedY < 0) {
            // The player is moving up
            speedY = speedY * 2 / 3 + getSpeedTimeDecrease() / 2;
        } else {
            if (this.isTouchingGround()) {
                //The player is running along the ground
                speedY = 0;
            } else {
                //The player is moving down
                this.speedY += getSpeedTimeDecrease();
            }
        }

        //changeToNextFrame();
        Log.d("Row: ", Integer.toString(row));
        Log.d("Col: ", Integer.toString(col));

        if(col==5){
            if(row<4){
                row++;
            }
            else{
                row=0;
            }
        }
        // manage frames
        /*
        if (row != 3) {
            // not dead
            if (speedY > getJumpSpeed() / 3) {
                row = 0;
            } else if (speedY > 0) {
                row = 1;
            } else {
                row = 2;
            }
        }*/
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    //If the player is dead
    public void dead() {
        this.isDead = true;
        this.speedY = getSpeedY() / 2;
        this.row = 3;
        this.frameTime = 3;
    }

    //Handles the jump action
    public void Jump() {
        //TODO fix the jump thing
        this.speedY = getJumpSpeed();
        this.y += getPosJumpIncrease();
    }

    //Handles the high jump action
    public void HighJump() {
        //TODO fix the jump thing
        this.speedY = getHighJumpSpeed();
        this.y += getPosHighJumpIncrease();
    }

    //Handles the slide action
    public void Slide() {
        //TODO add the slide mechanism
        this.speedY = getJumpSpeed();
        this.y += getPosJumpIncrease();
    }

    //Modifies Y speed on tap
    protected float getJumpSpeed() {
        //Log.d("Jump Speed", Float.toString(-view.getHeight() / 10f));
        return -view.getHeight() / 10f;     // -134 @ 720x1280 px
    }

    //Modifies Y coord on tap
    protected int getPosJumpIncrease() {
        Log.d("Jump Pos Inc", Float.toString(-view.getHeight() / 100));
        return -view.getHeight() / 100;     // -13 @ 720x1280 px
    }
    //Modifies Y speed on tap
    protected float getHighJumpSpeed() {
        Log.d("High Jump Speed", Float.toString(-view.getHeight() / 6f));
        return -view.getHeight() / 6f;     // -111 @ 720x1280 px
    }

    //Modifies Y coord on tap
    protected int getPosHighJumpIncrease() {
        Log.d("High Jump Pos Inc", Float.toString(-view.getHeight() / 50));
        return -view.getHeight() / 50;     // -13 @ 720x1280 px
    }

    protected float getSpeedTimeDecrease() {
        return view.getHeight() / 320;      // 4 @ 720x1280 px
    }

    public boolean isDead() {
        return this.isDead;
    }
}
