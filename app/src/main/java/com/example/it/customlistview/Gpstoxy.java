package com.example.it.customlistview;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

public class Gpstoxy {
    double Re = 6371.00877;     // 지도반경
    double grid = 5.0;            // 격자간격 (km)
    double slat1 = 30.0;           // 표준위도 1
    double slat2 = 60.0;           // 표준위도 2
    double olon = 126.0;          // 기준점 경도
    double olat = 38.0;           // 기준점 위도
    double xo = 210 / grid;   // 기준점 X좌표
    double yo = 675 / grid;   // 기준점 Y좌표
    double first = 0;
    double finalx,finaly;
    double lat,lon;

    public Gpstoxy(double lat,double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void cal(){
        double  PI=0, DEGRAD=0, RADDEG=0;
        double  re=0, olon=0, olat=0, sn=0, sf=0, ro=0;
        double slat1=0, slat2=0,xn=0, yn=0, ra=0, theta=0;

        if (this.first == 0) {
            PI = asin(1.0)*2.0;
            DEGRAD = PI / 180.0;
            RADDEG = 180.0 / PI;

            re = this.Re / this.grid;
            slat1 = this.slat1 * DEGRAD;
            slat2 = this.slat2 * DEGRAD;
            olon = this.olon * DEGRAD;
            olat = this.olat * DEGRAD;

            sn = tan(PI*0.25 + slat2*0.5) / tan(PI*0.25 + slat1*0.5);
            sn = log(cos(slat1) / cos(slat2)) / log(sn);
            sf = tan(PI*0.25 + slat1*0.5);
            sf = pow(sf, sn)*cos(slat1) / sn;
            ro = tan(PI*0.25 + olat*0.5);
            ro = re*sf / pow(ro, sn);
            this.first = 1;
        }

        ra = tan(PI*0.25+(lat)*DEGRAD*0.5);
        ra = re*sf/pow(ra,sn);
        theta = (lon)*DEGRAD - olon;
        if (theta >  PI) theta -= 2.0*PI;
        if (theta < -PI) theta += 2.0*PI;
        theta *= sn;
        finalx = (float)(ra*sin(theta)) + this.xo;
        finaly = (float)(ro - ra*cos(theta)) + this.yo;
    }

    public XYpoint getPoint(){
        XYpoint temp=new XYpoint((int)finalx,(int)finaly);
        return temp;
    }

}

