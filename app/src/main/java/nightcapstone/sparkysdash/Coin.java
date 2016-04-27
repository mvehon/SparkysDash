package nightcapstone.sparkysdash;

import android.graphics.Bitmap;

public class Coin extends Sprite {

    public static Bitmap coin_img;

    public Coin(GameView view, Game game) {
        super(view, game);

        //Reuse if bitmap already exists
        if (coin_img == null) {
            coin_img = getScaledBitmapAlpha8(game, R.drawable.coinspritesheet);
        }


        this.bitmap = coin_img;
        this.height = this.bitmap.getHeight();
        this.width = this.bitmap.getWidth() / (colNr = 24);                                      // 24 frames in a row
        this.frameTime = 1;                                                                     // the frame will change every tick
    }

    //Sets the x and y coordinates
    public void init(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        super.move();
    }


}
