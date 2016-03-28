package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;


public class Obstacle extends Sprite {
    private Football football;
    public boolean isAlreadyPassed = false;
    private float[] lane = new float[4];

    public Obstacle(GameView view, Game game) {
        super(view, game);

        //Generate a new obstacle
        football = new Football(view, game);

        //This is the y coordinate of the ground
        float theground = this.view.getHeight() - this.view.getHeight() * Background.GROUND_HEIGHT;

        //Divide the above ground area into four sections
        float aboveground = theground / 4;

        //Set the y locations of each lane
        lane[0] = theground - football.height;
        lane[1] = theground - (aboveground * 1) - (aboveground / 2);
        lane[2] = theground - (aboveground * 2) - (aboveground / 2);
        lane[3] = theground - (aboveground * 3) - (aboveground / 2);

        initPos();
    }


    //Puts the obstacle into its lane position
    private void initPos() {
        int x = football.lane;
        football.init(game.getResources().getDisplayMetrics().widthPixels, (int) lane[x]);
    }

    @Override
    public void draw(Canvas canvas) {
        football.draw(canvas);
    }

    @Override
    public boolean isOutOfRange() {
        return football.isOutOfRange();
    }

    @Override
    public boolean isColliding(Sprite sprite) {
        return football.isColliding(sprite);
    }

    @Override
    public void move() {
        football.move();
    }

    @Override
    public void setSpeedX(float speedX) {
        football.setSpeedX(speedX);
    }

    //If an obstacle has been passed
    @Override
    public boolean isPassed() {
        return football.isPassed();
    }

    //When an obstacle is passed
    public void onPass() {
        if (!isAlreadyPassed) {
            isAlreadyPassed = true;
            view.increasePoints();
        }
    }

    //When the player hits an obstacle
    @Override
    public void onCollision() {
        super.onCollision();
    }


}
