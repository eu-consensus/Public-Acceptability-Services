/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ViP
 */
public class BaselineAnalysisTools {
    public void prepareTools(){
        if(GlobalVarsStore.swn==null){
            System.out.println(GlobalVarsStore.printTime(new Date())+": Parsing wordNet... ");
            GlobalVarsStore.swn=new Tools.SentiWordNet();
            GlobalVarsStore.swn.populate(GlobalVarsStore.trainDataDir+"SentiWordNet_3.0.0_20130122.txt"); //load the SentiWordNet to memory
            System.out.println(GlobalVarsStore.printTime(new Date())+": WordNet parsed for "+GlobalVarsStore.swn.getWordCount()+" words. ");
        }else if(GlobalVarsStore.swn.getWordCount()<1){
            System.out.println(GlobalVarsStore.printTime(new Date())+": Parsing wordNet... ");
            GlobalVarsStore.swn=new SentiWordNet();
            GlobalVarsStore.swn.populate(GlobalVarsStore.trainDataDir+"SentiWordNet_3.0.0_20130122.txt"); //load the SentiWordNet to memory
            System.out.println(GlobalVarsStore.printTime(new Date())+": WordNet parsed for "+GlobalVarsStore.swn.getWordCount()+" words. ");
        }
    }
    
    public Double SentiWordNetMeanAnalysis(ArrayList<String> texts){
        int posCount=0;
        int neuCount=0;
        int negCount=0;
        ArrayList<Double> sents=new ArrayList<Double>();
        int step=(int)Math.round((double)texts.size()/100);
        //int completion=0;
        for (int j = 0; j < texts.size(); j++) {
            /*if(step>0){
                if(j%step==0) {
                    completion++;
                    System.out.println("Completion - "+completion+"%");
                }
            }*/
            String[] bag=texts.get(j).split("(?:(?:[^a-zA-Z]+')|(?:'[^a-zA-Z]+))|(?:[^a-zA-Z']+)");
            //DecimalFormat df = new DecimalFormat("#.####");
            ArrayList<Double> weights=new ArrayList<Double>();
            for (int i = 0; i < bag.length; i++) {
                Double res=GlobalVarsStore.swn.testWord(bag[i]);  
                if(res!=null){
                    weights.add(res);
                }
            }
            Double totalSent=0.0;
            for (int i = 0; i < weights.size(); i++) {
                totalSent+=weights.get(i);
            }
            if(weights.size()>0){
                totalSent=totalSent/weights.size();
                sents.add(totalSent);
            }
        }
        for (int i = 0; i < sents.size(); i++) {
            if(sents.get(i)>GlobalVarsStore.threshold[1]) posCount++;
            else if(sents.get(i)<GlobalVarsStore.threshold[0]) negCount++;
            else neuCount++;
        }
        System.out.println(posCount+" - "+neuCount+" - "+negCount);
        if(posCount+neuCount+negCount > 0)
        return ((double)posCount+(-1*negCount))/((double)posCount+neuCount+negCount); //return aggrevated score
        else
        return 0.0;
    }
}
