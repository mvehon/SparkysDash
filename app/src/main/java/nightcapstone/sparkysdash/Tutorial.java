package nightcapstone.sparkysdash;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Tutorial extends Sprite {
    public static Bitmap globalBitmap;

    public Tutorial(GameView view, Game game) {
        super(view, game);
        if (globalBitmap == null) {
            //globalBitmap = getScaledBitmapAlpha8(game, R.drawable.tutorial2);
            globalBitmap = BitmapFactory.decodeResource(game.getApplicationContext().getResources(), R.drawable.tutorial2);
            globalBitmap = globalBitmap.createScaledBitmap(globalBitmap,1750,972,false);
        }
        this.bitmap = globalBitmap;
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    //Sets the position of the view
    @Override
    public void move() {
        this.x = view.getWidth() / 2 - this.width / 2;
        this.y = view.getHeight() / 2 - this.height / 2;
    }

}
