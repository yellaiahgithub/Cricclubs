package com.cricket.utility;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.cricket.dao.ClubFactory;
import com.cricket.dto.ClubDto;
 
public class LongLatService {
     
    private static final String GEOCODE_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&";
    private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
     
    public static void main(String[] args) throws Exception {
 //       LongLatService.getLongitudeLatitude("1339 branchwood cir, naperville,IL,USA");
    }
     
    public static void getLongitudeLatitude(ClubDto club) throws Exception {
    		Thread.sleep(200);
        	String address = club.getAddress() + "," + club.getAddress1() + "," + club.getCity() + "," + club.getState() + "," + club.getCountry() + "," + club.getZipcode();
        	
        	String latLang = getLatitudeAndLongitude(address);
                
                club.setLatitude(latLang.split(",")[0]);
                club.setLongitude(latLang.split(",")[1]);
                
                ClubFactory.updateClubLatituteLongitude(club);
                 
    }
 
    public static String getLatitudeAndLongitude(String address){
    	

        StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
        if (address != null && !"".equals(address)) {
            try {
				urlBuilder.append("&address=").append(URLEncoder.encode(address, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
        }
        String strLatitude = "";
        String strLongtitude = "";
        final GetMethod getMethod = new GetMethod(urlBuilder.toString());
        try {
            httpClient.executeMethod(getMethod);
            Reader reader = new InputStreamReader(getMethod.getResponseBodyAsStream(), getMethod.getResponseCharSet());
             
            int data = reader.read();
            char[] buffer = new char[1024];
            Writer writer = new StringWriter();
            while ((data = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, data);
            }

            String result = writer.toString();

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader("<"+writer.toString().trim()));
            Document doc = db.parse(is);
         
       		strLatitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lat/text()");
			
             
            strLongtitude = getXpathValue(doc,"//GeocodeResponse/result/geometry/location/lng/text()");
            
            } catch (Exception e) {
            } finally {
            getMethod.releaseConnection();
        }

        
        return strLatitude + "," + strLongtitude;
    }
    
    private static String getXpathValue(Document doc, String strXpath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = null;
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
        }
        return resultData;
    }
     
}