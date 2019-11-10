package dev.sungmin.Shelter;

public class MapPoint {
    private String Name; //대피소 이름
    private String sisul_rddr; //도로명 주소
    private double  latitude; //위도
    private  double longitude; //경도

    public MapPoint() {
        super();
    }

    public  MapPoint(String Name, String sisul_rddr, double latitude, double longitude) {
        this.Name = Name;
        this.sisul_rddr = sisul_rddr;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    public String getSisul_rddr() {
        return sisul_rddr;
    }
    public  void setSisul_rddr(String sisul_rddr) {
        this.sisul_rddr = sisul_rddr;
    }

    public double getLatitude() {
        return latitude;
    }

    public  void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
