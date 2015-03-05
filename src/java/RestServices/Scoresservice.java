/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import Tools.BaselineAnalysisTools;
import Tools.CrawlersConnector;
import Tools.TopicAnalysisTools;
import Tools.databaseHandler;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.json.JSONException;

/**
 * REST Web Service
 *
 * @author ViP
 */
@Path("scores/service")
public class Scoresservice {

    @Context
    private UriInfo context;
    private databaseHandler dbh;

    /**
     * Creates a new instance of Scoresservice
     */
    public Scoresservice() {
        dbh = new databaseHandler();
        dbh.checkTables();
    }

    /**
     * Retrieves representation of an instance of RestServices.Scoresservice
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml(@QueryParam("scenario") String scenario, @QueryParam("objective") Integer objective) {
        //Reading source file
        CrawlersConnector ccn=new CrawlersConnector();
        ArrayList<String> scenarioTexts;
        try {
            scenarioTexts = ccn.readScenario(scenario);
        } catch (IOException ex) {
            return "<xml><result>Error: "+ex.getMessage()+"</result></xml>";
        } catch (JSONException ex) {
            return "<xml><result>Error: "+ex.getMessage()+"</result></xml>";
        }
        System.out.println("Parsed " + scenarioTexts.size() + " documents.");
        ArrayList<String> objectiveNames=new ArrayList<String>();
        objectiveNames.add("Change in Level of Service");
        objectiveNames.add("% change of Accidents cost");
        objectiveNames.add("% change of Air pollution (external) cost");
        objectiveNames.add("% change of Noise (external) cost");
        objectiveNames.add("User convenience in using the RP system");
        objectiveNames.add("Availability of alternative routes and modes");

        //Calculating SOF
        DecimalFormat df = new DecimalFormat("#.####");
        ArrayList<String> keys=null;
        if(scenario.equalsIgnoreCase("transportation")){
            if(objective==1){keys=ccn.transportKeywords1;}
            else if(objective==2){keys=ccn.transportKeywords2;}
            else if(objective==3){keys=ccn.transportKeywords3;}
            else if(objective==4){keys=ccn.transportKeywords4;}
            else if(objective==5){keys=ccn.transportKeywords5;}
            else if(objective==6){keys=ccn.transportKeywords6;}
        }else if(scenario.trim().toLowerCase().equals("biofuel")){
            if(objective==1){keys=ccn.biofuelKeywords1;}
            else if(objective==2){keys=ccn.biofuelKeywords2;}
            else if(objective==3){keys=ccn.biofuelKeywords3;}
            else if(objective==4){keys=ccn.biofuelKeywords4;}
        }else{
            return "Policy not defined. Available scenarios: 'transportation' and 'biofuel'.";
        }
        System.out.println("Calculating Score Of Frequency...");
        TopicAnalysisTools tat = new TopicAnalysisTools();
        ArrayList<Double> sofs = new ArrayList<Double>();
        for (int i = 0; i < keys.size(); i++) {
            sofs.add(tat.countFrequency(scenarioTexts, keys.get(i)));
        }
        Double sof=0.0;
        for (int i = 0; i < sofs.size(); i++) {
            sof+=sofs.get(i);
        }
        if(sofs.size()>0) sof=sof/sofs.size();
        System.out.println("Score of Frequency for objective '" + objectiveNames.get(objective-1) + "' is " + df.format(sof));

        //Calculating SOS
        System.out.println("Calculating Score Of Sentiment...");
        BaselineAnalysisTools bat = new BaselineAnalysisTools();
        bat.prepareTools();
        ArrayList<String> texts=null;
        Double sos=0.0;
        ArrayList<Double> soses = new ArrayList<Double>();
        for (int i = 0; i < keys.size(); i++) {
            try {
                texts=ccn.readKeyword(keys.get(i));
                soses.add(bat.SentiWordNetMeanAnalysis(texts));
            } catch (IOException ex) { ex.printStackTrace(); soses.add(0.0);
            } catch (JSONException ex) { ex.printStackTrace();soses.add(0.0);
            }
        }
        for (int i = 0; i < soses.size(); i++) {
            sos+=soses.get(i);
        }
        if(soses.size()>0) sos=sos/soses.size();
        System.out.println("Score of Sentiment for objective '" + objectiveNames.get(objective-1) + "' is " + df.format(sos));

        //Storing to database
        System.out.println("Storing to database..");
        int sucCount=0;
        for (int i = 0; i < keys.size(); i++) {
            boolean suc = false;
            try{
                suc = dbh.insert(keys.get(i), soses.get(i), sofs.get(i));
            }catch(Exception ex){}
            System.out.println("Operation Completed for keyword: " + keys.get(i) + ".");
            if (suc) {
                sucCount++;
            }
        }
        return "<xml><result>"+sucCount+" of "+keys.size()+" records successfully stored in database.</result></xml>";
    }
    
    public static void main(String[] args) {
        String scenario="biofuel";
        int objective=1;
        
        //Reading source file
        CrawlersConnector ccn=new CrawlersConnector();
        ArrayList<String> scenarioTexts = null;
        try {
            scenarioTexts = ccn.readScenario(scenario);
        } catch (IOException ex) {
            System.out.println("<xml><result>Error: "+ex.getMessage()+"</result></xml>");
        } catch (JSONException ex) {
            System.out.println("<xml><result>Error: "+ex.getMessage()+"</result></xml>");
        }
        System.out.println("Parsed " + scenarioTexts.size() + " documents.");
        ArrayList<String> objectiveNames=new ArrayList<String>();
        objectiveNames.add("Change in Level of Service");
        objectiveNames.add("% change of Accidents cost");
        objectiveNames.add("% change of Air pollution (external) cost");
        objectiveNames.add("% change of Noise (external) cost");
        objectiveNames.add("User convenience in using the RP system");
        objectiveNames.add("Availability of alternative routes and modes");

        //Calculating SOF
        DecimalFormat df = new DecimalFormat("#.####");
        ArrayList<String> keys=null;
        if(scenario.equalsIgnoreCase("transportation")){
            if(objective==1){keys=ccn.transportKeywords1;}
            else if(objective==2){keys=ccn.transportKeywords2;}
            else if(objective==3){keys=ccn.transportKeywords3;}
            else if(objective==4){keys=ccn.transportKeywords4;}
            else if(objective==5){keys=ccn.transportKeywords5;}
            else if(objective==6){keys=ccn.transportKeywords6;}
        }else if(scenario.equalsIgnoreCase("biofuel")){
            if(objective==1){keys=ccn.biofuelKeywords1;}
            else if(objective==2){keys=ccn.biofuelKeywords2;}
            else if(objective==3){keys=ccn.biofuelKeywords3;}
            else if(objective==4){keys=ccn.biofuelKeywords4;}
        }
        System.out.println("Calculating Score Of Frequency...");
        TopicAnalysisTools tat = new TopicAnalysisTools();
        ArrayList<Double> sofs = new ArrayList<Double>();
        for (int i = 0; i < keys.size(); i++) {
            sofs.add(tat.countFrequency(scenarioTexts, keys.get(i)));
        }
        Double sof=0.0;
        for (int i = 0; i < sofs.size(); i++) {
            sof+=sofs.get(i);
        }
        if(sofs.size()>0) sof=sof/sofs.size();
        System.out.println("Score of Frequency for objective '" + objectiveNames.get(objective-1) + "' is " + df.format(sof));

        //Calculating SOS
        System.out.println("Calculating Score Of Sentiment...");
        BaselineAnalysisTools bat = new BaselineAnalysisTools();
        bat.prepareTools();
        ArrayList<String> texts=null;
        Double sos=0.0;
        ArrayList<Double> soses = new ArrayList<Double>();
        for (int i = 0; i < keys.size(); i++) {
            try {
                texts=ccn.readKeyword(keys.get(i));
                soses.add(bat.SentiWordNetMeanAnalysis(texts));
            } catch (IOException ex) { ex.printStackTrace(); soses.add(0.0);
            } catch (JSONException ex) { ex.printStackTrace();soses.add(0.0);
            }
        }
        for (int i = 0; i < soses.size(); i++) {
            sos+=soses.get(i);
        }
        if(soses.size()>0) sos=sos/soses.size();
        System.out.println("Score of Sentiment for objective '" + objectiveNames.get(objective-1) + "' is " + df.format(sos));
    }

    /**
     * PUT method for updating or creating an instance of Scoresservice
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
}
