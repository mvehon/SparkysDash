package nightcapstone.sparkysdash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {
    private static final int DEFAULT_DENSITY = 1024;
    protected Bitmap bitmap;
    protected int height, width;
    protected int x, y;
    protected float speedX, speedY;
    protected Rect src;
    protected Rect dst;
    protected byte col, row;
    protected byte colNr = 1;
    protected short frameTime;
    protected short frameTimeCounter;

    protected GameView view;
    protected Game game;

    public Sprite(GameView view, Game game) {
        this.view = view;
        this.game = game;
        frameTime = 1;
        src = new Rect();
        dst = new Rect();
    }

    public void draw(Canvas canvas) {
        src.set(col * width, row * height, (col + 1) * width, (row + 1) * height);
        dst.set(x, y, x + width, y + height);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public void move() {
        changeToNextFrame();
        x += speedX;
        y += speedY;

    }

    protected void changeToNextFrame() {
        this.frameTimeCounter++;
        if (this.frameTimeCounter >= this.frameTime) {
            this.col = (byte) ((this.col + 1) % this.colNr);
            this.frameTimeCounter = 0;
        }
    }

    //Check if object is off the screen
    public boolean isOutOfRange() {
        return this.x + width < 0;
    }

    //Check if images are touching
    public boolean isColliding(Sprite sprite) {
        //Sprite = player
        //This = football
        if ((this.x + getCollisionTolerance() < sprite.x + sprite.width)
                && ((this.x + this.width) > (sprite.x + getCollisionTolerance() * 5 + 2))
                && this.y + getCollisionTolerance() < sprite.y + sprite.height
                && this.y + this.height > sprite.y + getCollisionTolerance()) {
            return true;
        }
        return false;
    }


    //Check if two locations are touching
    public boolean isTouching(int x, int y) {
        return (x > this.x && x < this.x + width
                && y > this.y && y < this.y + height);
    }

    //Handled in subclasses
    public void onCollision() {
    }


    //Check if it is touching the ground
    public boolean isTouchingGround() {
        return this.y + this.height > this.view.getHeight() - this.view.getHeight() * Background.GROUND_HEIGHT;
    }

    public boolean isPassed() {
        return this.x + this.width < view.getPlayer().getX();
    }

    public int getX() {
        return x;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    private int getCollisionTolerance() {
        return game.getResources().getDisplayMetrics().heightPixels / 50;
    }

    //These functions handle image scaling
    public static Bitmap getScaledBitmapAlpha8(Context context, int id) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
        bitmapOptions.inScaled = true;
        bitmapOptions.inDensity = DEFAULT_DENSITY;
        bitmapOptions.inTargetDensity = (int) (getScaleFactor(context) * DEFAULT_DENSITY);
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id, bitmapOptions);
        b.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        return b;
    }

    public static Bitmap getDownScaledBitmapAlpha8(Context context, int id) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
        bitmapOptions.inScaled = true;
        bitmapOptions.inDensity = DEFAULT_DENSITY;
        bitmapOptions.inTargetDensity = Math.min((int) (getScaleFactor(context) * DEFAULT_DENSITY), DEFAULT_DENSITY);
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id, bitmapOptions);
        b.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        return b;
    }

    public static float getScaleFactor(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels / 1066f;
    }

    //Handled in subclass
    public void init() {}


}
