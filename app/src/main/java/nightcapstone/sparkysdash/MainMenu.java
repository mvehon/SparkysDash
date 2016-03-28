package nightcapstone.sparkysdash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainMenu extends Activity {
    OptionsDialog optionsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button play = (Button) findViewById(R.id.playbtn);
        Button optionsbtn = (Button) findViewById(R.id.optionsbtn);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Game.class));
            }
        });


        optionsDialog = new OptionsDialog(this);
        optionsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsDialog.init();
                optionsDialog.show();
            }
        });
    }
}
