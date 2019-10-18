package dev.sungmin.Shelter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.location.Location;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private boolean TrackingMode = true;
    private TMapView tMapView = null;
    private TMapGpsManager tmapgps = null;
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();



    @Override
    public void onLocationChange(Location location) {
        if(TrackingMode){
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        //발급 받은 API키  보안상 문제로 인하여 깃허브 커밋시 지워서 커밋할것!
        tMapView.setSKTMapApiKey("API키");
        linearLayoutTmap.addView(tMapView);

        addPoint();
        MarkerPoint();

        /*현재 보는 방향으로 설정*/
        tMapView.setCompassMode(true);

        /*현 위치 아이콘 설정*/
        tMapView.setIconVisibility(true);

        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //네트워크에서 가져옴
        tmapgps.setProvider(tmapgps.GPS_PROVIDER); //GPS에서 가져옴
        tmapgps.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
    }

    public void addPoint() {
        mapPoints.add(new MapPoint("인문관",36.79880615906803, 127.07584122804188));
        mapPoints.add(new MapPoint("탕정중",36.8020487, 127.0651313));
        mapPoints.add(new MapPoint("체육관",36.7995895, 127.0710061));
    }

    public void MarkerPoint(){
        for(int i = 0; i<mapPoints.size(); i++) {
            TMapPoint point = new TMapPoint(mapPoints.get(i).getLatitude(),mapPoints.get(i).getLongitude());
            TMapMarkerItem markerItem1 = new TMapMarkerItem();

            markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
            markerItem1.setTMapPoint(point); // 마커의 좌표 지정
            markerItem1.setName("선문대학교"); // 마커의 타이틀 지정
            tMapView.addMarkerItem("markerItem1"+i, markerItem1); // 지도에 마커 추가

        }
    }
}
