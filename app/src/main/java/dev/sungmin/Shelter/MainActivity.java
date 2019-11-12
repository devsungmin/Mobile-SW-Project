package dev.sungmin.Shelter;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.widget.LinearLayout;
        import android.location.Location;
        import android.widget.Toast;

        import com.skt.Tmap.TMapGpsManager;
        import com.skt.Tmap.TMapView;
        import com.skt.Tmap.TMapMarkerItem;
        import com.skt.Tmap.TMapPoint;

        import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private static final int MY_PERMISSIONS_REQUEST = 1000;
    private boolean TrackingMode = true;
    private TMapView tMapView = null;
    private TMapGpsManager tmapgps = null;
    private static String TMapAPIKey = "앱";

    @Override
    public void onLocationChange(Location location) {
        if (TrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // 안드로이드 6.0 이상일 경우 퍼미션 체크
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setHttpsMode(true);
        //발급 받은 API키  보안상 문제로 인하여 깃허브 커밋시 지워서 커밋할것!
        tMapView.setSKTMapApiKey(TMapAPIKey);
        linearLayoutTmap.addView(tMapView);

        setUpMap();

        /*현재 보는 방향으로 설정*/
        tMapView.setCompassMode(true);

        /*현 위치 아이콘 설정*/
        tMapView.setIconVisibility(true);

        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapgps = new TMapGpsManager(MainActivity.this);
        tmapgps.setMinTime(1000);
        tmapgps.setMinDistance(5);
        tmapgps.setProvider(tmapgps.NETWORK_PROVIDER); //네트워크에서 가져옴
        tmapgps.OpenGps();

        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);
    }

    private boolean checkPermissions() {
        /* 파일 접근 권한 체크 */
        int CheckFLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int CheckCLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (CheckFLocation != PackageManager.PERMISSION_GRANTED && CheckCLocation != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 승인이 필요합니다", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
                Toast.makeText(this, "사용자위치를 받아오기 위해 GPS 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    private void setUpMap() {
        ShelterApi parser = new ShelterApi();
        ArrayList<MapPoint> mapPoint = new ArrayList<MapPoint>();
        try {
            mapPoint = parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mapPoint.size(); i++) {
            for (MapPoint entity : mapPoint) {
                TMapPoint point = new TMapPoint(mapPoint.get(i).getLatitude(), mapPoint.get(i).getLongitude());
                TMapMarkerItem markerItem1 = new TMapMarkerItem();

                markerItem1.setPosition(0.5f, 1.0f);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(entity.getName());

                /* 마커 풍선창 이벤트 */
                markerItem1.setCanShowCallout(true);
                markerItem1.setCalloutTitle(mapPoint.get(i).getName());
                markerItem1.setCalloutSubTitle(mapPoint.get(i).getSisul_rddr());

                tMapView.setCenterPoint(mapPoint.get(i).getLongitude(), mapPoint.get(i).getLatitude());
                tMapView.addMarkerItem("markerItem1" + i, markerItem1);
            }
        }
    }
}