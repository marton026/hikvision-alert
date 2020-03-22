package com.marton.hikalert.controllers;

import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

@Component
public class HikCommand {
    private int i = 1;
    private ModemConnection modemConnection;
    public boolean ifStart;
//    private URL url;
//    private String usern;
//    private String passw;
//    private int freq;


    public HikCommand(ModemConnection modemConnection) {

        this.modemConnection = modemConnection;
    }

    public void textChange(URL url, String textOver, String id, Boolean enabled, String posx, String posy, String usern, String passw) throws IOException {

        String data = "<TextOverlay xmlns=\"http://www.hikvision.com/ver10/XMLSchema\" version=\"1.0\">\n"
                + "<id>" + id + "</id>\n"
                + "<enabled>" + enabled + "</enabled>\n"
                + "<posX>" + posx + "</posX>\n"
                + "<posY>" + posy + "</posY>\n"
                + "<message>" + textOver + "</message>\n"
                + "</TextOverlay>";
        connect(url, data, usern, passw);
    }

    public void motionDetection(URL url, Boolean enable, String usern, String passw, String sensitive) throws IOException {

        String data = "<MotionDetection xmlns=\"http://www.hikvision.com/ver10/XMLSchema\" version=\"1.0\">\n" +
                "<id>1</id>\n" +
                "<enabled>" + enable + "</enabled>\n" +
                "<regionType>grid</regionType>\n" +
                "<Grid>\n" +
                "<rowGranularity>18</rowGranularity>\n" +
                "<columnGranularity>22</columnGranularity>\n" +
                "</Grid>\n" +
                "<MotionDetectionRegionList>\n" +
                "<sensitivityLevel>" + sensitive + "</sensitivityLevel>\n" +
                "</MotionDetectionRegionList>\n" +
                "</MotionDetection>\n";
        connect(url, data, usern, passw);
    }


    public void connect(URL url, String data, String usern, String passw) throws IOException {
        HttpURLConnection httpConnection = prepareConn(url, "PUT", null, usern, passw);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        httpConnection.setRequestMethod("PUT");
        httpConnection.setRequestProperty("Content-Type", "text/xml");
        OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
        out.write(data);
        out.flush();
        out.close();
        System.out.println("Printing......");
        System.out.println(httpConnection.getResponseCode());
        System.out.println(httpConnection.getResponseMessage());
        InputStreamReader reader = new InputStreamReader(httpConnection.getInputStream());
        StringBuilder buf = new StringBuilder();
        char[] cbuf = new char[2048];
        int num;

        while (-1 != (num = reader.read(cbuf))) {
            buf.append(cbuf, 0, num);
        }
        String result = buf.toString();
        System.out.println("\nResponse received from server after POST" + result);
    }


    public void eventDetect(URL url, String usern, String passw, int frequencyTime) throws IOException {
        HttpURLConnection con = prepareConn(url, "GET", null, usern, passw);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "text/xml");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()
            ));

           // System.out.println(ifStart);
            String  inputLine;

            while ((inputLine = in.readLine()) != null && ifStart) {
              //  System.out.println("Czy działa     = "+ifStart);
                if (inputLine.equals("<eventState>active</eventState>")) {
                    System.out.println(i++ + "Wykryto ruch");
                    if (i == frequencyTime) {
                        System.out.println(" ------- Wykryto ruch. Wysłano SMS-a ---------");
                       // modemConnection.sendSMS("883277383", "Motion Detection from Hkvision");
                    }
                }
                if (inputLine.equals("<eventState>inactive</eventState>")) {
                    System.out.println("Brak ruchu");
                    i = 0;
                }
            }


            in.close();
            con.disconnect();
        } else {
            System.out.println("Żądanie GET nie odpowiada");
        }
    }

    public void stopStart(URL url, String usern, String passw, int frequencyTime) {
        new Thread(() ->{
            while (true) {
                if (ifStart) {
                    try {
                        eventDetect(url,usern,passw,frequencyTime);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("STOP");
                    break;
                }
                }
        }).start();
    }


    private HttpURLConnection prepareConn(final URL url, String method, Properties request_props, String username, String password) throws Error, IOException {
        System.out.println("Autoryzacja......");
        if (!url.getProtocol().equalsIgnoreCase("http"))
            throw new Error(url.toString() + " Niepoprawny url!");

        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(300);
        conn.setRequestMethod(method);
        final Properties DEFAULT_REQUEST_PROPS = new Properties();
        DEFAULT_REQUEST_PROPS.setProperty("charset", "utf-8");

        final Properties props = new Properties(DEFAULT_REQUEST_PROPS);
        if (request_props != null)
            for (final String name : request_props.stringPropertyNames())
                props.setProperty(name, request_props.getProperty(name));

        for (final String name : props.stringPropertyNames())
            conn.setRequestProperty(name, props.getProperty(name));
        if (null != username && null != password)
            conn.setRequestProperty("Authorization", "Basic " + new BASE64Encoder().encode((username + ":" + password).getBytes()));
        return conn;
    }
}
