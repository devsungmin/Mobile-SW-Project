package dev.sungmin.Shelter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class ShelterApi {
    private static String ServiceKey = "API키";
    public ShelterApi() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<MapPoint> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<MapPoint> mapPoint = new ArrayList<MapPoint>();

        String facility_name = null, longitude= null,latitude=null;
        boolean bfacility_name = false, blatitude = false, blongitude = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if(tag.equals("facility_name")) {
                    bfacility_name = true;
                }
                if(tag.equals("latitude")) {
                    blatitude = true;
                }
                if(tag.equals("longitude")){
                    blongitude = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(bfacility_name == true){
                    facility_name = xpp.getText();
                    bfacility_name = false;
                } else if(blatitude == true){
                    latitude = xpp.getText();
                    blatitude = false;
                }else if(blongitude ==true){
                    longitude = xpp.getText();
                    blongitude = false;
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("row")) {
                    MapPoint entity = new MapPoint();
                    entity.setName(facility_name);
                    entity.setLatitude(Double.valueOf(latitude));
                    entity.setLongitude(Double.valueOf(longitude));
                    mapPoint.add(entity);
                    System.out.println(mapPoint.size());
                }
            }
            event_type = xpp.next();
        }
        System.out.println(mapPoint.size());

        return mapPoint;
    }

    private String getURLParam(String search){
        String url = "http://apis.data.go.kr/1741000/CivilDefenseShelter2/getCivilDefenseShelterList?ServiceKey=" + ServiceKey + "&type=xml&pageNo=1&numOfRows=300&flag=Y";
        return url;
    }

    public static void main(String[] args) {
        new ShelterApi();
    }
}
