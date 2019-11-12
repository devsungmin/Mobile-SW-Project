package dev.sungmin.Shelter;

public class MapPoint {
    private String Name; //대피소 이름
    private String sisul_rddr; //도로명 주소
    private double  latitude; //위도
    private double longitude; //경도
    private double b_addr_cd; //법정동 주소코드

    public MapPoint() {
        super();
    }

    public  MapPoint(String Name, String sisul_rddr, double latitude, double longitude, double b_addr_cd) {
        this.Name = Name;
        this.sisul_rddr = sisul_rddr;
        this.latitude = latitude;
        this.longitude = longitude;
        this.b_addr_cd = b_addr_cd;
    }

    /* 대피소 이름 */
    public String getName() {
        return Name;
    }

    public void setName(String Name){
        this.Name = Name;
    }

    /* 도로명 주소 */
    public String getSisul_rddr() {
        return sisul_rddr;
    }

    public  void setSisul_rddr(String sisul_rddr) {
        this.sisul_rddr = sisul_rddr;
    }

    /* 위도 */
    public double getLatitude() {
        return latitude;
    }

    public  void setLatitude(double latitude){
        this.latitude = latitude;
    }

    /* 경도 */
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /* 법정동 주소 코드 */
    public double getBAddrCd() {
        return b_addr_cd;
    }

    public void setBAddrCd(double b_addr_cd) {
        this.b_addr_cd = b_addr_cd;
    }

}
