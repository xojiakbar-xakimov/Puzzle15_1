package uz.gita.puzzle15_1.app;

import android.app.Application;

import uz.gita.puzzle15_1.pref.MyPref;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyPref.init(this);

    }
}
