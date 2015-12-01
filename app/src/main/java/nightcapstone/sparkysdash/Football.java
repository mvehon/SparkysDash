package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Random;

public class Football extends Sprite {

    public static Bitmap globalBitmap;
    public int lane;

    public Football(GameView view, Game game) {
        super(view, game);

        //Randomly decide which 'lane' to position in
        Random random = new Random();
        lane = random.nextInt(4);
        Log.d("Lane", Integer.toString(lane));

        if (globalBitmap == null) {
            //TODO set conditional here for lane=0 to be ground obstacle image
            //TODO put animation handling here
            globalBitmap = getScaledBitmapAlpha8(game, R.drawable.footballresize);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    //Sets the x and y coordinates
    public void init(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
