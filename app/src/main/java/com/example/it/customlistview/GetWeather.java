package com.example.it.customlistview;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class GetWeather extends Thread{
    int x,y;
    URL url=null;
    String weather;
    ArrayList<HashMap<String,Object>> weatherList=new ArrayList<HashMap<String,Object>> ();
    String errorMsg;

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            StringBuilder sb=new StringBuilder();
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;
                sb.append(line);
            }
            weather=sb.toString();
            convertxml();
        }
        catch(Exception e){
            errorMsg="날씨를 받아오는데 오류가 생겼습니다.";
        }
    }
    public GetWeather(int x,int y) throws Exception {
        this.x=x;
        this.y=y;

        this.url = new URL(getApiAddress());
    }

    public void convertxml(){
        HashMap<String, Object> data = new HashMap<String, Object>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentbuilder = factory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(weather.getBytes());
            Document doc = documentbuilder.parse(is);
            Element element = doc.getDocumentElement();
            NodeList list1 = element.getElementsByTagName("data");
            double windchill_temp=0.0,windchill_ws=0.0;
            for (int i = 0; i < list1.getLength(); i++) {
                for (Node node = list1.item(i).getFirstChild(); node != null;node = node.getNextSibling())
                {
                    if (node.getNodeName().equals("hour")) { //시간
                        data = new HashMap<String, Object>();
                        data.put("시간", node.getTextContent());
                    }
                    if (node.getNodeName().equals("temp")) { //현재온도
                        String temp= node.getTextContent();
                        data.put("현재온도", temp);
                        windchill_temp=Double.parseDouble(temp);
                    }
                    if (node.getNodeName().equals("tmx")) { //최고온도
                        data.put("최고온도", node.getTextContent());
                    }
                    if (node.getNodeName().equals("tmn")) { //최저온도
                        data.put("최저온도", node.getTextContent());
                    }
                    if (node.getNodeName().equals("pop")) { //강수확률
                        data.put("강수확률", node.getTextContent());
                    }
                    if (node.getNodeName().equals("ws")) { //풍속
                        String ws = node.getTextContent();
                        data.put("풍속", ws);
                        windchill_ws=Double.parseDouble(ws);
                        data.put("체감온도",String.valueOf(Math.round(cal_windChill(windchill_temp,windchill_ws*3.6))));

                    }
                    if(node.getNodeName().equals("wfKor")){
                        data.put("날씨한국어",node.getTextContent());
                    }
                    if (node.getNodeName().equals("reh")) { //습도
                        data.put("습도",node.getTextContent());
                        this.weatherList.add(data);
                    }
                }
            }

        } catch (Exception e) {
            errorMsg="파싱에러";
        }
    }

    public double cal_windChill(double temp,double ws){
        return 13.12+0.6215*temp-11.37*Math.pow(ws,0.16)+0.3965*Math.pow(ws,0.16)*temp;
    }
    private String getApiAddress() {
        String apiURL="http://www.kma.go.kr/wid/queryDFS.jsp?gridx="+x+"&gridy="+y;
        return apiURL;
    }

    public ArrayList<HashMap<String,Object>> returnWeather() {
        return weatherList;
    }
}