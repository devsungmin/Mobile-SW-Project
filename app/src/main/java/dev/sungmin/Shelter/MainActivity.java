package dev.sungmin.Shelter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.LinearLayout;
import android.location.Location;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    private boolean TrackingMode = true;
    private TMapView tMapView = null;
    private TMapGpsManager tmapgps = null;
    private static String TMapAPIKey = "앱";
    private static String DataAPIKEY = "API키";
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
        tMapView.setSKTMapApiKey(TMapAPIKey);
        linearLayoutTmap.addView(tMapView);

        addPoint();
        MarkerPoint();

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

//    좌표
    public void addPoint() {
        mapPoints.add(new MapPoint("인문관",36.79880615906803, 127.07584122804188));
        mapPoints.add(new MapPoint("탕정중",36.8020487, 127.0651313));
        mapPoints.add(new MapPoint("체육관",36.7995895, 127.0710061));
    }

//    마커
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

//    공공데이터 파싱
    protected void RestApi(String result) {
        String facility_name = null, sisul_rddr = null, sisul_addr = null , longitude = null, latitude = null, shelter_psbl = null;
        boolean bfacility_name = false, bsisul_rddr = false, bsisul_addr = false, blongitude = false, blatitude = false, bshelter_psbl = false;

        StrictMode.enableDefaults();
        StringBuffer buffer = new StringBuffer();
        try{
            String urlstr = "http://apis.data.go.kr/1741000/CivilDefenseShelter2/getCivilDefenseShelterList?ServiceKey=" + DataAPIKEY;
            URL url = new URL(urlstr);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();

            HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setRequestMethod("GET");
//            br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(),"UTF-8"));

            while((eventType != XmlPullParser.END_DOCUMENT)) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    String tag = xpp.getName();
                    if (tag.equals("facility_name")) { //대피소 이름
                        buffer.append("이름 : ");
                        buffer.append(xpp.getText());
                        bfacility_name = true;
                        xpp.next();
                        buffer.append(xpp.getText());
                        buffer.append("\n");
                    } else if (tag.equals("sisul_rddr")) { //대피소 도로명 주소
                        buffer.append("도로명 주소 : ");
                        buffer.append(xpp.getText());
                        bsisul_rddr = true;
                        xpp.next();
                        buffer.append("\n");
                    } else if (tag.equals("sisul_addr")) { //대피소 번지명 주소
                        buffer.append("번지명 주소 : ");
                        buffer.append(xpp.getText());
                        bsisul_addr = true;
                        xpp.next();
                        buffer.append("\n");
                    } else if (tag.equals("longitude")) { //위도
                        blongitude = true;
                        xpp.next();
                        buffer.append(xpp.getText());
                        buffer.append("\n");
                    } else if (tag.equals("latitude")) { //경도
                        blatitude = true;
                        xpp.next();
                        buffer.append(xpp.getText());
                        buffer.append("\n");
                    } else if (tag.equals("shelter_psbl")) { //수용 인원
                        buffer.append("수용인원 : ");
                        buffer.append(xpp.getText());
                        bshelter_psbl = true;
                        xpp.next();
                        buffer.append("\n");
                    }
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
