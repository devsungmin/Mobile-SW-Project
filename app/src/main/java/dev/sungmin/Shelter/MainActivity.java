package dev.sungmin.Shelter;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.PointF;
        import android.graphics.drawable.BitmapDrawable;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.StrictMode;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.location.Location;
        import android.widget.Toast;

        import com.skt.Tmap.TMapData;
        import com.skt.Tmap.TMapGpsManager;
        import com.skt.Tmap.TMapPOIItem;
        import com.skt.Tmap.TMapPolyLine;
        import com.skt.Tmap.TMapView;
        import com.skt.Tmap.TMapMarkerItem;
        import com.skt.Tmap.TMapPoint;

        import java.util.ArrayList;

        import static android.graphics.Color.RED;


public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private static final int MY_PERMISSIONS_REQUEST = 1000;
    private boolean TrackingMode = true;
    private TMapView tMapView = null;
    private TMapGpsManager tmapgps = null;
    private Location lastKnownLocation = null;
    private static String TMapAPIKey = "APIKEY";
    private double longitude, latitude, longitude2, latitude2;

    /* 메뉴 바 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

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


        //네트워크 좌표값 설정
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(locationListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        lm.requestLocationUpdates("network", 0, 0, locationListener);

        // 안드로이드 6.0 이상일 경우 퍼미션 체크
        if (Build.VERSION.SDK_INT >= 23) {
            //checkPermissions();
        }

        LinearLayout linearLayoutTmap = (LinearLayout) findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);
        tMapView.setHttpsMode(true);
        //발급 받은 API키  보안상 문제로 인하여 깃허브 커밋시 지워서 커밋할것!
        tMapView.setSKTMapApiKey(TMapAPIKey);
        linearLayoutTmap.addView(tMapView);

        setUpMap();
        Button navi = findViewById(R.id.navi);
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TMapPoint tMapPointStart = new TMapPoint(latitude, longitude); // SKT타워(출발지)
                TMapPoint tMapPointEnd = new TMapPoint(latitude2, longitude2); // N서울타워(목적지)
                try {
                    TMapPolyLine tMapPolyLine = new TMapData().findPathData(tMapPointStart, tMapPointEnd);
                    tMapPolyLine.setLineColor(RED);
                    tMapPolyLine.setLineWidth(2);
                    tMapView.addTMapPolyLine("Line1", tMapPolyLine);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
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

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                latitude2 = markerItem.latitude;
                longitude2 = markerItem.longitude;
            }
        });

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
                final TMapMarkerItem markerItem1 = new TMapMarkerItem();

                markerItem1.setPosition(0.5f, 1.0f);
                markerItem1.setTMapPoint(point);
                markerItem1.setName(entity.getName());

                /* 마커 풍선창 이벤트 */
                markerItem1.setCanShowCallout(true);
                markerItem1.setCalloutTitle(mapPoint.get(i).getName());
                markerItem1.setCalloutSubTitle(mapPoint.get(i).getSisul_rddr());

                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.kkk);           // 그림을  비트맵형식으로 변환 하기위해 bitmapdraw 에 바인딩
                Bitmap b = bitmapdraw.getBitmap();                                                                    // 비트맵 선언
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);


                markerItem1.setCalloutRightButtonImage(smallMarker);

                tMapView.setCenterPoint(mapPoint.get(i).getLongitude(), mapPoint.get(i).getLatitude());
                tMapView.addMarkerItem("markerItem1" + i, markerItem1);
            }
        }
    }

    /* 메뉴바 선택 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_btn1:
                //게시판
                finish();
                startActivity(new Intent(MainActivity.this,BoardActivity.class));
                return true;
            case R.id.action_btn2:
                //개발자
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Get the last location, and update UI
            lastKnownLocation = location;
            longitude = lastKnownLocation.getLongitude();
            latitude = lastKnownLocation.getLatitude();
            lm.removeUpdates(this);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
}
