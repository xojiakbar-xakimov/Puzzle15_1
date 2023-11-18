package uz.gita.puzzle15_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import uz.gita.puzzle15_1.pref.MyPref;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnPlay ;
    private AppCompatButton btnInfo ;
    private AppCompatButton btnContinue ;
    private SharedPreferences myPref ;
    private MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPref = MyPref.getShared() ;

        init() ;
        onClick(myPref.getInt("indexJ" , 0)) ;
        mediaPlayer= MediaPlayer.create(this , R.raw.click3) ;

    }

    private void init() {
        btnContinue = findViewById(R.id.btnContinue) ;
        btnPlay = findViewById(R.id.btnPlay) ;
        btnInfo = findViewById(R.id.btn_info) ;
    }

    private void onClick(int check) {

        btnInfo.setOnClickListener(v -> {
            mediaPlayer.start();
            startActivity(new Intent(this , InfoActivity.class));
            finish();
        }) ;
        if (check != 0 && myPref.getInt("count" , 0) != 0) {
            btnContinue.setVisibility(View.VISIBLE);
            btnPlay.setText("New Game");
            btnContinue.setOnClickListener(v -> {
                mediaPlayer.start();
                startActivity(new Intent(this , PlayActivity.class));
                finish();
            });
            btnPlay.setOnClickListener(v -> {
                mediaPlayer.start();
                myPref.edit().putInt("indexJ" , 0).apply();
                myPref.edit().putBoolean("shuffle" , true).apply();
                startActivity(new Intent(this , PlayActivity.class)) ;
                finish();
            });
        }else {
            btnPlay.setOnClickListener(v -> {
                mediaPlayer.start();
                myPref.edit().putBoolean("shuffle" , true).apply();
                startActivity(new Intent(this , PlayActivity.class)) ;
                finish();
            });
        }
    }

}