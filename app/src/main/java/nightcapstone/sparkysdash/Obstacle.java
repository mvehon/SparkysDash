package nightcapstone.sparkysdash;

import android.graphics.Canvas;

import java.util.Random;


public class Obstacle extends Sprite {
    Football football=null;
    Coin coin=null;
    public boolean isAlreadyPassed = false;
    public float[] lane = new float[4];
    public boolean isFootball = true;
    int objlane=1;
    int arraysize;

    public Obstacle(GameView view, Game game) {
        super(view, game);


        Random random = new Random();
        arraysize = view.obstacles.size();
        Float coinchance = random.nextFloat();

        //Coin chance, 10% for 1 football, 25% for 2, 50% for 3
        switch(arraysize){
            case 0:
                if(coinchance<0.10){
                    isFootball=false;
                }
                break;
            case 1:
                if(coinchance<0.25){
                    isFootball=false;
                }
                break;
            case 2:
                if(coinchance<0.50){
                    isFootball=false;
                }
                break;
        }


        objlane = random.nextInt(4);


        if(arraysize>=1){
            //Make sure there are no impossible configurations
            makePossible();
        }


        //This is the y coordinate of the ground
        float theground = this.view.getHeight() - this.view.getHeight() * Background.GROUND_HEIGHT;

        //Divide the above ground area into four sections
        float aboveground = theground / 4;


        //Generate a new obstacle
        if (isFootball) {
            if (objlane == 0) {
                //Generate a hurdle
                football = new Football(view, game, 0);
            }else {
                //Generate a football
                football = new Football(view, game);
            }
        }
        else{
            //Make a coin
            coin = new Coin(view, game);
        }

        if(coin!=null){
            this.height = coin.height;
        }
        else{
            this.height = football.height;
        }

        //Set the y locations of each lane
        lane[0] = theground - this.height;
        lane[1] = theground - (aboveground * 1) - (aboveground / 2);
        lane[2] = theground - (aboveground * 2) - (aboveground / 2);
        lane[3] = theground - (aboveground * 3) - (aboveground / 2);

        initPos();
    }


    //Puts the obstacle into its lane position
    private void initPos() {
        if (coin != null) {
            coin.init(game.getResources().getDisplayMetrics().widthPixels, (int) lane[objlane]);
        } else
            football.init(game.getResources().getDisplayMetrics().widthPixels, (int) lane[objlane]);

    }

    @Override
    public void draw(Canvas canvas) {
        if (coin != null) {
            coin.draw(canvas);
        } else
            football.draw(canvas);
    }

    @Override
    public boolean isOutOfRange() {
        if (coin != null) {
            return coin.isOutOfRange();
        }
        return football.isOutOfRange();
    }

    @Override
    public boolean isColliding(Sprite sprite) {
        if (coin != null) {
            return coin.isColliding(sprite);
        }
        return football.isColliding(sprite);
    }

    @Override
    public void move() {
        //this.move();
        if (coin != null) {
            coin.move();
        } else
            football.move();
    }

    @Override
    public void setSpeedX(float speedX) {
        //this.setSpeedX(speedX);
        if (coin != null) {
            coin.setSpeedX(speedX);
        } else
            football.setSpeedX(speedX);
    }

    //If an obstacle has been passed
    @Override
    public boolean isPassed() {
        if (coin != null) {
            return coin.isPassed();
        }
        return football.isPassed();
    }

    //When an obstacle is passed
    public void onPass() {
        if (!isAlreadyPassed) {
            isAlreadyPassed = true;
            view.increasePoints(50);
        }
    }

    //When the player hits an obstacle
    @Override
    public void onCollision() {
        super.onCollision();
    }

    public void makePossible() {
        Random rand = new Random();
        if (arraysize == 1) {
            int obpos1 = view.obstacles.get(0).objlane;
            boolean obtype1 = view.obstacles.get(0).isFootball;
            while (obpos1 == objlane) {
                objlane = rand.nextInt(4);
            }
            if ((obpos1 == 0 || obpos1 == 2) && (objlane == 0 || objlane == 2)) {
                objlane++;
            }
        }
    }
}
