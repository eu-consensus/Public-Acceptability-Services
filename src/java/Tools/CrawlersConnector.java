/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ViP
 */
public class CrawlersConnector {
    public ArrayList<String> transportKeywords1;
    public ArrayList<String> transportKeywords2;
    public ArrayList<String> transportKeywords3;
    public ArrayList<String> transportKeywords4;
    public ArrayList<String> transportKeywords5;
    public ArrayList<String> transportKeywords6;
    public ArrayList<String> biofuelKeywords1;
    public ArrayList<String> biofuelKeywords2;
    public ArrayList<String> biofuelKeywords3;
    public ArrayList<String> biofuelKeywords4;

    public CrawlersConnector() {
        //Change in Level of Service
        transportKeywords1=new ArrayList<String>();
        transportKeywords1.add("congestion");
        transportKeywords1.add("traffic jam");
        transportKeywords1.add("traffic");
        transportKeywords1.add("road capacity");
        transportKeywords1.add("low speed");
        transportKeywords1.add("perceived safety");
        transportKeywords1.add("road safety");
        transportKeywords1.add("driving safety");
        transportKeywords1.add("traffic comfort");
        
        //% change of Accidents cost
        transportKeywords2=new ArrayList<String>();
        transportKeywords2.add("road accident");
        transportKeywords2.add("car accident");
        transportKeywords2.add("vehicle accident");
        transportKeywords2.add("road kill");
        transportKeywords2.add("roadkill");
        transportKeywords2.add("road injury");
        transportKeywords2.add("accident probability");
        transportKeywords2.add("road safety");
        transportKeywords2.add("driving safety");
        transportKeywords2.add("vehicle insurance");
        transportKeywords2.add("car insurance");
        
        //% change of Air pollution (external) cost
        transportKeywords3=new ArrayList<String>();
        transportKeywords3.add("air pollution");
        transportKeywords3.add("car pollution");
        transportKeywords3.add("vehicle pollution");
        transportKeywords3.add("car emissions");
        transportKeywords3.add("vehicle emissions");
        transportKeywords3.add("traffic environmental impacts");
        transportKeywords3.add("gaseous pollutants");
        transportKeywords3.add("health congestion");
        transportKeywords3.add("traffic climate");
        transportKeywords3.add("traffic health");
        transportKeywords3.add("greenhouse effect");

        //% change of Noise (external) cost
        transportKeywords4=new ArrayList<String>();
        transportKeywords4.add("traffic noise");
        transportKeywords4.add("noise pollution");
        transportKeywords4.add("sound pollution");
        transportKeywords4.add("traffic vibrations");
        transportKeywords4.add("traffic environmental impacts");

        //User convenience in using the RP system
        transportKeywords5=new ArrayList<String>();
        transportKeywords5.add("toll payment");
        transportKeywords5.add("toll collection");
        transportKeywords5.add("toll plaza");
        transportKeywords5.add("toll traffic");
        transportKeywords5.add("toll congestion");
        transportKeywords5.add("queue blocking");
        transportKeywords5.add("automatic tolls");
        transportKeywords5.add("electronic tolls");

        //Availability of alternative routes and modes
        transportKeywords6=new ArrayList<String>();
        transportKeywords6.add("toll payment");
        transportKeywords6.add("toll roads monopoly");
        transportKeywords6.add("toll road alternative");
        transportKeywords6.add("toll road alternatives");
        transportKeywords6.add("toll traffic divert");
        transportKeywords6.add("non toll roads");
        transportKeywords6.add("national road network");
        transportKeywords6.add("rural network");
        transportKeywords6.add("toll exceptions");
        
        //biodiversity
        biofuelKeywords1=new ArrayList<String>();
        biofuelKeywords1.add("biodiversity");
        biofuelKeywords1.add("riparian areas");
        biofuelKeywords1.add("degraded land");
        biofuelKeywords1.add("land degration");
        biofuelKeywords1.add("soil erosion");
        biofuelKeywords1.add("yield intensification");
        biofuelKeywords1.add("agricultural intensification");
        biofuelKeywords1.add("loss species");
        biofuelKeywords1.add("non-renewable water resources");
        
        //CO2 Emissions
        biofuelKeywords2=new ArrayList<String>();
        biofuelKeywords2.add("climate change mitigation");
        biofuelKeywords2.add("environmental pollution fuel");
        biofuelKeywords2.add("GHG emissions");
        biofuelKeywords2.add("ILUC");
        biofuelKeywords2.add("indirect land use change");
        biofuelKeywords2.add("land use change");
        biofuelKeywords2.add("land based biofuels");
        biofuelKeywords2.add("first generation biofuels");
        biofuelKeywords2.add("advanced biofuels");
        biofuelKeywords2.add("second generation biofuels");
        
        //Forest Land
        biofuelKeywords3=new ArrayList<String>();
        biofuelKeywords3.add("conversion forest");
        biofuelKeywords3.add("soil erosion");
        biofuelKeywords3.add("deforestation");
        biofuelKeywords3.add("forest land");
        biofuelKeywords3.add("forest");
        biofuelKeywords3.add("trees");
        biofuelKeywords3.add("degraded land");
        biofuelKeywords3.add("soil erosion");
        
        //Price of Food
        biofuelKeywords4=new ArrayList<String>();
        biofuelKeywords4.add("food crops");
        biofuelKeywords4.add("energy crops");
        biofuelKeywords4.add("agricultural crops");
        biofuelKeywords4.add("land rights");
        biofuelKeywords4.add("land conflicts");
        biofuelKeywords4.add("land grabbing");
        biofuelKeywords4.add("food prices");
        biofuelKeywords4.add("food security");
        biofuelKeywords4.add("food availability");
        biofuelKeywords4.add("animal feed co-products");
    }
    
    public ArrayList<String> readScenario(String scenario) throws MalformedURLException, IOException, JSONException{
        ArrayList<String> res=new ArrayList<String>();
        ArrayList<String> allkeys=new ArrayList<String>();
        if(scenario.trim().toLowerCase().equals("transportation")){
            allkeys.addAll(transportKeywords1);
            allkeys.addAll(transportKeywords2);
            allkeys.addAll(transportKeywords3);
            allkeys.addAll(transportKeywords4);
            allkeys.addAll(transportKeywords5);
            allkeys.addAll(transportKeywords6);
        }else if(scenario.trim().toLowerCase().equals("biofuel")){
            allkeys.addAll(biofuelKeywords1);
            allkeys.addAll(biofuelKeywords2);
            allkeys.addAll(biofuelKeywords3);
            allkeys.addAll(biofuelKeywords4);
        }
        //String keychain="";
        for (int i = 0; i < allkeys.size(); i++) {
            Date now=new Date();
            Calendar c = Calendar.getInstance(); 
            c.setTime(now); 
            c.add(Calendar.MONTH, -6);
            Date before6months=c.getTime();
            String url="http://consensus.atc.gr:8080/mongo-handler/rest/search?keywords="+allkeys.get(i)+"-link&time="+before6months.getTime();
            url=url.replace(" ","%20");
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String text="";
            String line="";
            while((line=rd.readLine()) != null){
                text+=line;
            }

            JSONArray as=new JSONArray(text);
            for (int j = 0; j < as.length(); j++) {
                res.add(((JSONObject)as.get(j)).getString("title"));
            }
        }
        //keychain=keychain.substring(0,keychain.length()-1);
        
        
        return res;
    }
    
    public ArrayList<String> readObjective(String scenario,int objective) throws MalformedURLException, IOException, JSONException{
        ArrayList<String> res=new ArrayList<String>();
        ArrayList<String> allkeys=new ArrayList<String>();
        if(scenario.trim().toLowerCase().equals("transportation")){
            if(objective==1){allkeys=transportKeywords1;}
            else if(objective==2){allkeys=transportKeywords2;}
            else if(objective==3){allkeys=transportKeywords3;}
            else if(objective==4){allkeys=transportKeywords4;}
            else if(objective==5){allkeys=transportKeywords5;}
            else if(objective==6){allkeys=transportKeywords6;}
        }else if(scenario.trim().toLowerCase().equals("biofuel")){
            if(objective==1){allkeys=biofuelKeywords1;}
            else if(objective==2){allkeys=biofuelKeywords2;}
            else if(objective==3){allkeys=biofuelKeywords3;}
            else if(objective==4){allkeys=biofuelKeywords4;}
        } 
        
        String keychain="";
        for (int i = 0; i < allkeys.size(); i++) {
            keychain+=allkeys.get(i)+",";
        }
        keychain=keychain.substring(0,keychain.length()-1);
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.MONTH, -6);
        Date before6months=c.getTime();
        String url="http://consensus.atc.gr:8080/mongo-handler/rest/search?keywords="+keychain+"&time="+before6months.getTime();
        url=url.replace(" ","%20");
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String text="";
        String line="";
        while((line=rd.readLine()) != null){
            text+=line;
        }
        
        JSONArray as=new JSONArray(text);
        for (int i = 0; i < as.length(); i++) {
            res.add(((JSONObject)as.get(i)).getString("title"));
        }
        
        return res;
    }
    
    public ArrayList<String> readKeyword(String keyword) throws MalformedURLException, IOException, JSONException{
        ArrayList<String> res=new ArrayList<String>();
        Date now=new Date();
        Calendar c = Calendar.getInstance(); 
        c.setTime(now); 
        c.add(Calendar.MONTH, -6);
        Date before6months=c.getTime();
        String url="http://consensus.atc.gr:8080/mongo-handler/rest/search?keywords="+keyword+"&time="+before6months.getTime();
        url=url.replace(" ","%20");
        InputStream is = new URL(url).openStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String text="";
        String line="";
        while((line=rd.readLine()) != null){
            text+=line;
        }
        
        JSONArray as=new JSONArray(text);
        for (int i = 0; i < as.length(); i++) {
            res.add(((JSONObject)as.get(i)).getString("title"));
        }
        
        return res;
    }
}
