package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sparky extends Sprite {
    public static Bitmap globalBitmap;
    protected boolean isDead = false;

    public Sparky(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            globalBitmap = getScaledBitmapAlpha8(game, R.drawable.sparkysprite);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth() / (colNr = 8);          // 8 frames in a row
        this.height = this.bitmap.getHeight() / 4;                  // 4 frames in a column
        this.frameTime = 3;                                         // the frame will change every 3 runs
        this.y = game.getResources().getDisplayMetrics().heightPixels / 2 - this.height / 2;    // Start position in in the middle of the screen
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

        changeToNextFrame();

        // manage frames
        if (row != 3) {
            // not dead
            if (speedY > getTapSpeed() / 3) {
                row = 0;
            } else if (speedY > 0) {
                row = 1;
            } else {
                row = 2;
            }
        }
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
        this.speedY = getTapSpeed();
        this.y += getPosTapIncrease();
    }

    //Handles the slide action
    public void Slide() {
        //TODO add the slide mechanism
        this.speedY = getTapSpeed();
        this.y += getPosTapIncrease();
    }

    //Modifies Y speed on tap
    protected float getTapSpeed() {
        return -view.getHeight() / 16f;     // -80 @ 720x1280 px
    }

    //Modifies Y coord on tap
    protected int getPosTapIncrease() {
        return -view.getHeight() / 100;     // -12 @ 720x1280 px
    }

    protected float getSpeedTimeDecrease() {
        return view.getHeight() / 320;      // 4 @ 720x1280 px
    }

    public boolean isDead() {
        return this.isDead;
    }
}
