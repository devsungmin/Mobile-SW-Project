package dev.sungmin.Shelter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

public class ShelterApi {
    private static String ServiceKey = "APIKEY";
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

        /* 이름, 경도, 위도, 도로명주소, 법정동 주소코드 */
        String facility_name = null, longitude = null,latitude = null, sisul_rddr = null, b_addr_cd = null;
        boolean bfacility_name = false, blatitude = false, blongitude = false, bsisul_rddr = false, bb_addr_cd = false;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
                if(tag.equals("facility_name")) {
                    bfacility_name = true;
                }
                if(tag.equals("sisul_rddr")) {
                    bsisul_rddr = true;
                }
                if(tag.equals("latitude")) {
                    blatitude = true;
                }
                if(tag.equals("longitude")) {
                    blongitude = true;
                }
                if(tag.equals("b_addr_cd")) {
                    bb_addr_cd = true;
                }
            } else if (event_type == XmlPullParser.TEXT) {
                if(bfacility_name == true){
                    facility_name = xpp.getText();
                    bfacility_name = false;
                } else if(bsisul_rddr == true) {
                    sisul_rddr = xpp.getText();
                    bsisul_rddr = false;
                }else if(blatitude == true){
                    latitude = xpp.getText();
                    blatitude = false;
                }else if(blongitude ==true){
                    longitude = xpp.getText();
                    blongitude = false;
                } else  if(bb_addr_cd == true) {
                    b_addr_cd = xpp.getText();
                    System.out.println(b_addr_cd);
                    bb_addr_cd = false;
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("row")) {
                    MapPoint entity = new MapPoint();
                    entity.setName(facility_name);
                    entity.setSisul_rddr(sisul_rddr);
                    entity.setLatitude(Double.valueOf(latitude));
                    entity.setLongitude(Double.valueOf(longitude));
                    entity.setBAddrCd(Double.valueOf(b_addr_cd));
                    mapPoint.add(entity);
                }
            }
            event_type = xpp.next();
        }

        return mapPoint;
    }

    private String getURLParam(String search){
        String url = "http://apis.data.go.kr/1741000/CivilDefenseShelter2/getCivilDefenseShelterList?ServiceKey=" + ServiceKey + "&type=xml&pageNo=1&numOfRows=100&flag=Y";
        return url;
    }

    public static void main(String[] args) {
        new ShelterApi();
    }
}