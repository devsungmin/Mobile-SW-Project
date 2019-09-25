package dev.sungmin.Shelter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;


public class MainActivity extends AppCompatActivity {
    LinearLayout linearLayoutTmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        TMapView tmapview = new TMapView(this);
        //발급 받은 API키  보안상 문제로 인하여 깃허브 커밋시 지워서 커밋할것!
        tmapview.setSKTMapApiKey("API_KEY");
        linearLayoutTmap.addView(tmapview);
    }
}
