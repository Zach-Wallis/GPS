package au.edu.federation.gps;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    private Button start;
    private Button settings;
    private Button help;
    private Button close;
    private Button ping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final MediaPlayer pingSound = MediaPlayer.create(this, R.raw.sonar_ping_sound_effect);

        start = (Button) findViewById(R.id.button1);
        settings = (Button) findViewById(R.id.button2);
        help = (Button) findViewById(R.id.button3);
        close = (Button) findViewById(R.id.button4);
        ping = (Button) findViewById(R.id.button);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, LobbySelect.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Pop.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Pop.class));
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeApp();
            }
        });
        ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pingSound.start();
            }
        });
    }


    public void closeApp(){
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
