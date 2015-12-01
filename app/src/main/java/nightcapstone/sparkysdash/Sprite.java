package nightcapstone.sparkysdash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

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

    public Sprite(GameView view, Game game){
        this.view = view;
        this.game = game;
        frameTime = 1;
        src = new Rect();
        dst = new Rect();
    }
    public void draw(Canvas canvas){
        src.set(col*width, row*height, (col+1)*width, (row+1)*height);
        dst.set(x, y, x+width, y+height);
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public void move(){
        changeToNextFrame();
        x+= speedX;
        y+= speedY;

    }

    protected void changeToNextFrame(){
        this.frameTimeCounter++;
        if(this.frameTimeCounter >= this.frameTime){
            this.col = (byte) ((this.col+1) % this.colNr);
            this.frameTimeCounter = 0;
        }
    }

    //Check if object is off the screen
    public boolean isOutOfRange(){
        return this.x + width < 0;
    }

    //Check if images are touching
    public boolean isColliding(Sprite sprite){
        if(this.x + getCollisionTolerance() < sprite.x + sprite.width
                && this.x + this.width > sprite.x + getCollisionTolerance()
                && this.y + getCollisionTolerance() < sprite.y + sprite.height
                && this.y + this.height > sprite.y + getCollisionTolerance()){
            return true;
        }
        return false;
    }

    //The radius for collision
    public boolean isCollidingRadius(Sprite sprite, float factor){
        int m1x = this.x+(this.width>>1);
        int m1y = this.y+(this.height>>1);
        int m2x = sprite.x+(sprite.width>>1);
        int m2y = sprite.y+(sprite.height>>1);
        int dx = m1x - m2x;
        int dy = m1y - m2y;
        int d = (int) Math.sqrt(dy*dy + dx*dx);

        if(d < (this.width + sprite.width) * factor
                || d < (this.height + sprite.height) * factor){
            return true;
        }else{
            return false;
        }
    }

    //Check if two locations are touching
    public boolean isTouching(int x, int y){
        return (x  > this.x && x  < this.x + width
                && y  > this.y && y < this.y + height);
    }

    //Handled in subclasses
    public void onCollision(){
    }

    //TODO decide what to do with this
    //public boolean isTouchingEdge(){		return isTouchingGround() || isTouchingSky();	}

    //Check if it is touching the ground
    public boolean isTouchingGround(){
        return this.y + this.height > this.view.getHeight() - this.view.getHeight() * Background.GROUND_HEIGHT;
    }

    //Check if it is touching the sky
    public boolean isTouchingSky(){
        return this.y < 0;
    }

    public boolean isPassed(){
        return this.x + this.width < view.getPlayer().getX();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public int getWidth() {
        return width;
    }

    private int getCollisionTolerance(){
        // 25 @ 720x1280 px
        return game.getResources().getDisplayMetrics().heightPixels / 50;
    }

    //These functions handle image scaling
    public static Bitmap getScaledBitmapAlpha8(Context context, int id) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
        bitmapOptions.inScaled = true;
        bitmapOptions.inDensity = DEFAULT_DENSITY;
        bitmapOptions.inTargetDensity = (int)(getScaleFactor(context)*DEFAULT_DENSITY);
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id, bitmapOptions);
        b.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        return b;
    }

    public static Bitmap getDownScaledBitmapAlpha8(Context context, int id) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inPreferredConfig = Bitmap.Config.ALPHA_8;
        bitmapOptions.inScaled = true;
        bitmapOptions.inDensity = DEFAULT_DENSITY;
        bitmapOptions.inTargetDensity = Math.min((int)(getScaleFactor(context)*DEFAULT_DENSITY), DEFAULT_DENSITY);
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id, bitmapOptions);
        b.setDensity(context.getResources().getDisplayMetrics().densityDpi);
        return b;
    }

    public static float getScaleFactor(Context context){
        // 1.2 @ 720x1280 px
        return context.getResources().getDisplayMetrics().heightPixels / 1066f;
    }
}
