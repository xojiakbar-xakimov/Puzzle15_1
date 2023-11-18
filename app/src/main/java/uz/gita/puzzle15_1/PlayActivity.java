package uz.gita.puzzle15_1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import java.util.ArrayList;
import java.util.Collections;
import uz.gita.puzzle15_1.pref.MyPref;

public class PlayActivity extends AppCompatActivity {

    private RelativeLayout relativeLayoutPlay ;
    private AppCompatButton[][] views = new AppCompatButton[4][4] ;
    private ArrayList<Integer> numbers = new ArrayList<>() ;
    private ArrayList<Integer> getNumbers ;
    private Chronometer chronometer ;
    private AppCompatTextView tvCount ;
    private AppCompatImageView btnExit ;
    private AppCompatImageView btnRestart ;
    private int count ;
    private int indexI ;
    private int indexJ ;
    private Boolean check = true ;
    private Boolean checkTwo = true ;
    private Boolean checkPause = true ;
    private MediaPlayer mediaPlayer  ;
    private AppCompatImageView btnVolume ;
    private SharedPreferences myPref ;
    private AppCompatImageView btnPause ;
    private AppCompatTextView winCount ;
    private Chronometer winChro ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mediaPlayer = MediaPlayer.create(this , R.raw.click3) ;
        myPref = MyPref.getShared() ;


        setAllViewsId() ;
        setAllBtnId() ;
        setNumbers() ;
        shuffle(numbers);
        onClick();
        setWin() ;

    }

    private void setAllViewsId() {
        chronometer = findViewById(R.id.chronometer) ;
        tvCount = findViewById(R.id.tv_count);
        btnExit = findViewById(R.id.btn_exit) ;
        btnRestart = findViewById(R.id.restart) ;
        btnVolume = findViewById(R.id.btn_volume) ;
        btnPause = findViewById(R.id.btnPause) ;
        winChro = findViewById(R.id.winChro);
        winCount = findViewById(R.id.winCount) ;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this  , MainActivity.class));
        finish();
    }

    private void setWin() {
        winCount.setText(String.valueOf(myPref.getInt("winCount" , 0)));
        winChro.setBase(myPref.getLong("winTime" , 0) + SystemClock.elapsedRealtime());
    }

    @Override
    protected void onResume() {
        if (myPref.getInt("indexJ" , 0) == 0) {
            myPref.edit().putBoolean("shuffle" , true).apply();
            tvCount.setText(String.valueOf(count));
            setChronometerTime();
            views[indexI][indexJ].setVisibility(View.VISIBLE);
            setValue(numbers);
        }else {
            if (myPref.getBoolean("checkTwo" , true)) {
                btnVolume.setImageResource(R.drawable.volume);
            }else {
                btnVolume.setImageResource(R.drawable.mute);
            }
            count = myPref.getInt("count" , 0) ;
            myPref.edit().putBoolean("shuffle" , false).apply();
            tvCount.setText(String.valueOf(count));
            String[] numbersS = myPref.getString("numbers" , "" ).split("/") ;
            long time = myPref.getLong("time" , 0) ;
            chronometer.setBase(SystemClock.elapsedRealtime() + time);
            getNumbers=  new ArrayList<>();
            Log.d("TTT" , "indexJ") ;
            for (int i = 0 ; i < numbersS.length ; i++) {
                if (!numbersS[i].equals("0")  && !numbersS[i].equals("")) {
                    getNumbers.add(Integer.parseInt(numbersS[i]));
                }else {
                    getNumbers.add(0);
                }
            }
                chronometer.start();
            numbers = getNumbers ;
            setValue(getNumbers);

            if (myPref.getBoolean("checkPause" , false)) {
                showDialogPause();
            }
        }

        super.onResume();
    }

    private void isWin() {
        int check = 0  ;
        for (int i = 0; i < 15; i++) {
            if (views[i / 4][i % 4].getText().equals(String.valueOf(i + 1 ))) {
                check++;
            }
        }
        if (check == 15) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_win , null  , false);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();

            dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
            AppCompatButton button = view.findViewById(R.id.playAgainBtn) ;
            long nowTime = chronometer.getBase() - SystemClock.elapsedRealtime() ;
            chronometer.stop();
            button.setOnClickListener(v -> {
                shuffle(numbers);
                views[indexI][indexJ].setVisibility(View.VISIBLE);
                count = 0 ;
                tvCount.setText(String.valueOf(0));
                chronometer.setBase(SystemClock.elapsedRealtime());
                setValue(numbers);
                setWin();
                dialog.dismiss();
            });

            ((TextView)view.findViewById(R.id.dialog_num)).setText(String.valueOf(count)) ;
            ((Chronometer)view.findViewById(R.id.chronometer_dialog)).setBase(chronometer.getBase());
            dialog.show();

            int nowCount = count ;
            if (myPref.getLong("winTime" , 0) > nowTime || myPref.getInt("winCount" , 0) > nowCount) {
                myPref.edit().putLong("winTime" , nowTime).apply();
                myPref.edit().putInt("winCount" , nowCount).apply();
            }
        }
    }

    private void setAllBtnId() {
        relativeLayoutPlay = findViewById(R.id.relativePlay);
        for (int i = 0 ; i < relativeLayoutPlay.getChildCount() ; i++) {
            views[i / 4][i % 4] = (AppCompatButton) relativeLayoutPlay.getChildAt(i) ;

            saveDataId(views[i / 4][i % 4] , i / 4 , i % 4 , i) ;
            views[i / 4][i % 4].setOnClickListener(v -> {
                DataId dataId = (DataId) v.getTag() ;
                changeStep(dataId.i , dataId.j);
                saveChanges();
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntegerArrayList("list" , numbers) ;

        outState.putInt("count" , count);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getNumbers = new ArrayList<>(savedInstanceState.getIntegerArrayList("list")) ;

        count = savedInstanceState.getInt("count") ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        StringBuilder stringBuilder = new StringBuilder() ;
        for (int i = 0 ; i < 16 ; i++) {
            if (!views[i / 4][i % 4].getText().toString().equals("0")){
                stringBuilder.append(views[i/4][i%4].getText().toString() + "/") ;
            }else {
                stringBuilder.append("0/") ;
            }
        }
        myPref.edit().putInt("count" , count).apply();
        long time = chronometer.getBase() - SystemClock.elapsedRealtime();
        myPref.edit().putLong("time" , time).apply();
        myPref.edit().putString("numbers" , stringBuilder.toString()).apply();
        myPref.edit().putInt("indexJ" , indexJ).apply();

    }

    private void saveChanges() {
        myPref.edit().putBoolean("shuffle" , false).apply();
        StringBuilder stringBuilder = new StringBuilder() ;
        for (int i = 0 ; i < 16 ; i++) {
            if (!views[i / 4][i % 4].getText().toString().equals("")){
                stringBuilder.append(views[i/4][i%4].getText().toString() + "/") ;
            }else {
                stringBuilder.append("0/") ;
            }
        }
        myPref.edit().putInt("count" , count).apply();
        long time = chronometer.getBase() - SystemClock.elapsedRealtime();
        myPref.edit().putLong("time" , time).apply();
        myPref.edit().putString("numbers" , stringBuilder.toString()).apply();
        myPref.edit().putInt("indexJ" , 2).apply();
    }
    private void setNumbers() {
        for (int i = 0 ; i < 16 ; i++) {
            numbers.add(i) ;
        }
    }

    private void shuffle(ArrayList<Integer> numbers) {
        Collections.shuffle(numbers);
        while (!isSolvable(numbers)) {
            Collections.shuffle(numbers);
        }
    }

    private boolean isSolvable(ArrayList<Integer> list) {
        int countInversions = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 15; j++) {
                if (list.get(i) > list.get(j) && j > i) {
                    countInversions++;
                }
            }
        }
        return countInversions % 2 == 0;
    }
    private void setValue(ArrayList<Integer> numbers) {
        if(!isSolvable(numbers) && myPref.getBoolean("shuffle" , true)) {
            shuffle(numbers);
            setValue(numbers);
        }
        for (int i = 0 ; i < numbers.size() ; i++) {
            if (numbers.get(i) == 0) {
                views[i / 4][i % 4].setVisibility(View.INVISIBLE);
                indexI = i / 4 ;
                indexJ = i % 4 ;
            }else {
                views[i / 4][i % 4].setText(String.valueOf(numbers.get(i)));
            }
        }
    }
    private void saveDataId(AppCompatButton appCompatButton , int i , int j , int a) {
        appCompatButton.setTag(new DataId(i , j , a));
    }

    private void changeStep(int i , int j) {

        if (canAccess(i , j) && checkPause) {
            views[indexI][indexJ].setText(views[i][j].getText());
            views[i][j].setText("0");
            views[i][j].setVisibility(View.INVISIBLE);
            views[indexI][indexJ].setVisibility(View.VISIBLE);
            myPref.edit().putInt("indexi" , i).apply();
            myPref.edit().putInt("indexj" , j).apply();
            indexI = i ;
            indexJ = j ;
            count++ ;

            if (myPref.getBoolean("checkTwo" , true)) {

                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(this, R.raw.click3);
                mediaPlayer.start();
            }


            tvCount.setText(String.valueOf(count));
            loadNumbersChange();
            isWin();
        }
    }

    private void loadNumbersChange() {

        if (views != null) {
            ArrayList<Integer> list = new ArrayList<>() ;
            for (int i = 0 ; i < 16 ; i++) {
                int a = Integer.parseInt(views[i / 4][i % 4].getText().toString());
                list.add(a);
            }
            numbers = list ;
        }
    }
    private boolean canAccess(int i , int j) {
        return  (((Math.abs(indexI - i) == 1) && indexJ == j) || ((Math.abs(indexJ - j) == 1) && indexI == i)) ;
    }

    private void setChronometerTime() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void onClick() {
        btnExit.setOnClickListener(v -> {
            startActivity(new Intent(this , MainActivity.class));
            finish() ;
        });
        btnRestart.setOnClickListener(v -> {
            mediaPlayer = MediaPlayer.create(this , R.raw.click_restart) ;
            mediaPlayer.start();
            shuffle(numbers);
            views[indexI][indexJ].setVisibility(View.VISIBLE);
            count = 0 ;
            tvCount.setText(String.valueOf(0));
            chronometer.setBase(SystemClock.elapsedRealtime());
            setValue(numbers);
        });

        btnVolume.setOnClickListener(v -> {
            if (myPref.getBoolean("checkTwo" , true)) {
                btnVolume.setImageResource(R.drawable.mute);
                myPref.edit().putBoolean("checkTwo" , false).apply();
                check = false ;
            }else {
                btnVolume.setImageResource(R.drawable.volume);
                myPref.edit().putBoolean("checkTwo" , true).apply();
                check = true ;
            }
        });

        btnPause.setOnClickListener(v -> {

            showDialogPause();

        });
    }

    private void showDialogPause() {
        myPref.edit().putLong("timeP" , chronometer.getBase() - SystemClock.elapsedRealtime()).apply();
        chronometer.stop();
        myPref.edit().putBoolean("checkPause" , true).apply();
        PopupDialog.getInstance(this)
                .setStyle(Styles.ALERT)
                .setHeading("Pause")
                .setDescription("Game paused. Take a moment to catch your breath,"+ // Game paused. Take a moment to catch your breath, review your progress, or attend to other matters
                        " review your progress, or attend to other matters.")
                .setCancelable(false)
                .showDialog(new OnDialogButtonClickListener() {
                    @Override
                    public void onDismissClicked(Dialog dialog) {
                        chronometer.setBase(SystemClock.elapsedRealtime() + myPref.getLong("timeP" , 0));
                        chronometer.start();
                        dialog.dismiss();
                        myPref.edit().putBoolean("checkPause" , false).apply();
                        super.onDismissClicked(dialog);
                    }
                });
    }

}

