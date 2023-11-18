package uz.gita.puzzle15_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    String text = "<div>\n" +
            "        <h1 style=\"text-align : center ;\">\n" +
            "            Puzzle 15\n" +
            "        </h1>\n" +
            "        \n" +
            "        <ul>\n" +
            "            <li stlye=\"text-justify :inter-word ;\">\n" +
            "                \tThis app was created by a student of Gita academy whose name is Xojiakbar and this app helps you to have enjoyable moments while you are playing that game .\n" +
            "            </li> \n" +
            "            <li>\n" +
            "                \tWhile you are playing that game, if music is not comfortable for you, you can turn off voice of music and continue your game.\n" +
            "            </li> \n" +
            "            <li>\n" +
            "                \tIf you exit that app before finishing you can go on your last game or start new game.\n" +
            "            </li>\n" +
            "        </ul>\n" +
            "        <div>\n" +
            "            <h3>\n" +
            "                Framework\n" +
            "            </h3>\n" +
            "            <ul>\n" +
            "                <li>\n" +
            "                        Android Studio\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                        Java\n" +
            "                </li>\n" +
            "                    <div>\n" +
            "                        <h3> \n" +
            "                            Used technologies\n" +
            "                        </h3>\n" +
            "                        <ul>\n" +
            "                            <li>\n" +
            "                                SavedInstances\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                MediaPlayer\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                SharedPreference\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                AlertDialog\n" +
            "                            </li>\n" +
            "                        </ul>\n" +
            "                    </div>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>" ;
    TextView textView ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        findViewById(R.id.btn_back).setOnClickListener(v-> {
            startActivity(new Intent(this  , MainActivity.class));
            finish();
        });

        textView = findViewById(R.id.tv_info) ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else
            textView.setText(Html.fromHtml(text));

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this  , MainActivity.class));
        finish();
    }
}
