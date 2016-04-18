package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Coin extends Sprite {

    public static Bitmap coin_img;

    public Coin(GameView view, Game game) {
        super(view, game);
        if (coin_img == null) {
            coin_img = getScaledBitmapAlpha8(game, R.drawable.coinspritesheet2);
        }
        this.bitmap = coin_img;
        //this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();

        this.width = this.bitmap.getWidth() / (colNr = 24);                                      // 10 frames in a row
        this.frameTime = 1;                                                                     // the frame will change every 3 runs
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
