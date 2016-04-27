package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background extends Sprite {
    public static Bitmap bg_img;
    
    //This is a small float ~= .215 used as a marker for the portion of screen where ground is
    public static final float GROUND_HEIGHT = (1f * 155) / 720; 

    //Set the image used for the background
    public Background(GameView view, Game game) {
        super(view, game);

        //Reuse if bitmap already exists
        if (bg_img == null) {
            bg_img = getDownScaledBitmapAlpha8(game, R.drawable.bg3);
        }

        this.bitmap = bg_img;
    }

    //This handles the scrolling and wrapping of the background image
    @Override
    public void draw(Canvas canvas) {
        double factor = (1.0 * canvas.getHeight()) / bitmap.getHeight();

        if (-x > bitmap.getWidth()) {
            x += bitmap.getWidth();
        }

        int endBitmap = Math.min(-x + (int) (canvas.getWidth() / factor), bitmap.getWidth());
        int endCanvas = (int) ((endBitmap + x) * factor) + 1;
        src.set(-x, 0, endBitmap, bitmap.getHeight());
        dst.set(0, 0, endCanvas, canvas.getHeight());
        canvas.drawBitmap(this.bitmap, src, dst, null);

        //Draw another copy of background
        if (endBitmap == bitmap.getWidth()) {
            src.set(0, 0, (int) (canvas.getWidth() / factor), bitmap.getHeight());
            dst.set(endCanvas, 0, endCanvas + canvas.getWidth(), canvas.getHeight());
            canvas.drawBitmap(this.bitmap, src, dst, null);
        }
    }


}
