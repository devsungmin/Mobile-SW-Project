package dev.sungmin.Shelter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        TMapView tmapview = new TMapView(this);
        //발급 받은 API키  보안상 문제로 인하여 깃허브 커밋시 지워서 커밋할것!
        tmapview.setSKTMapApiKey("API키");
        linearLayoutTmap.addView(tmapview);


        TMapMarkerItem markerItem1 = new TMapMarkerItem();

        TMapPoint tMapPoint1 = new TMapPoint(36.79880615906803, 127.07584122804188); // 선문대학교 인문관
        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
        markerItem1.setName("선문대학교"); // 마커의 타이틀 지정
        tmapview.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

    }
}
