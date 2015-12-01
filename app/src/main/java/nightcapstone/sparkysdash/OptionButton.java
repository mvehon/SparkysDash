package nightcapstone.sparkysdash;

//The option button
public class OptionButton extends Sprite{
    public OptionButton(GameView view, Game game) {
        super(view, game);
        this.bitmap = getScaledBitmapAlpha8(game, R.drawable.settings);
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
    }

    //Sets the location of the button
    @Override
    public void move(){
        this.x = this.view.getWidth() - this.width-30;
        this.y = 30;
    }
}
