package nightcapstone.sparkysdash;

import android.graphics.Bitmap;

public class Football extends Sprite {

    public static Bitmap ball_img;
    public static Bitmap hurdle_img;

    public Football(GameView view, Game game) {
        super(view, game);

        //Reuse if bitmap already exists
        if (ball_img == null) {
            ball_img = getScaledBitmapAlpha8(game, R.drawable.footballsheet);
        }

        this.bitmap = ball_img;
        this.height = this.bitmap.getHeight();
        this.width = this.bitmap.getWidth() / (colNr = 10);                                             // 10 frames in a row
        this.frameTime = 1;                                                                             // the frame will change every frame
    }

    public Football(GameView view, Game game, int ln) {
        super(view, game);

        //Reuse if bitmap already exists
        if (hurdle_img == null) {
            hurdle_img = getScaledBitmapAlpha8(game, R.drawable.hurdle);
        }

        this.bitmap = hurdle_img;
        this.height = this.bitmap.getHeight();
        this.width = this.bitmap.getWidth() / (colNr = 1);
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
