package uz.gita.puzzle15_1.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPref {
    static private MyPref myPref ;
    public SharedPreferences sharedPreferences ;

    private MyPref(Context context) {
        sharedPreferences = context.getSharedPreferences("MyPref" , Context.MODE_PRIVATE) ;
    }

    static public void init(Context context) {
        if (myPref == null) myPref = new MyPref(context) ;
    }

    static public SharedPreferences getShared() {
        return myPref.sharedPreferences;
    }
}
