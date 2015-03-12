/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Entities.Afinn;
import Entities.Lexicon;
import Entities.SentiWordNet;
import java.util.Date;

/**
 *
 * @author ViP
 */
public class GlobalVarsStore {
    //User Specified Variables
    //public static String databaseUsername="USERNAME";
    //public static String databasePassword="PASSWORD";
    //public static String databaseServer="jdbc:mysql://SERVER_IP/DATABASE";
    //public static String databaseDriver=null; /*DATABASE DRIVER or NULL*/
    public static String databasePool="consensus_pool_resource";
    public static String trainDataDir = "/trainData/";
    
    //System Variables
    public static SentiWordNet swn;
    public static String[] contradictionWords = {"but","instead","however"};
    public static String backupFilename;
    public static int backupPreSent;
    public static Double[] threshold=new Double[]{-0.1,0.1};
    public static Lexicon lex;
    public static String lexicon="afinn";
    public static String lexiPath;
    
    public static String printTime(Date date) {
        return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }
    
    public static void trainLexicon(String lexicon,String path){
        lexicon=lexicon.toLowerCase().trim();
        if(GlobalVarsStore.lexicon.equals(lexicon)){
            if(GlobalVarsStore.lex==null){
                if(lexicon.equals("wordnet")){
                    GlobalVarsStore.lex=new SentiWordNet();
                }else if(lexicon.equals("afinn")){
                    GlobalVarsStore.lex=new Afinn();
                }
            }
        }else{
            GlobalVarsStore.lexicon=lexicon.toLowerCase().trim();
            if(lexicon.equals("wordnet")){
                GlobalVarsStore.lex=new SentiWordNet();
            }else if(lexicon.equals("afinn")){
                GlobalVarsStore.lex=new Afinn();
            }
        }
        if(GlobalVarsStore.lexiPath==null){
            GlobalVarsStore.lexiPath=path;
            System.out.println(GlobalVarsStore.printTime(new Date())+": Parsing "+lexicon+"... ");
            GlobalVarsStore.lex.populate(path);
            System.out.println(GlobalVarsStore.printTime(new Date())+": "+lexicon+" parsed for "+GlobalVarsStore.lex.getWordCount()+" words. ");
        }else if(!GlobalVarsStore.lexiPath.equals(path)){
            GlobalVarsStore.lexiPath=path;
            System.out.println(GlobalVarsStore.printTime(new Date())+": Parsing "+lexicon+"... ");
            GlobalVarsStore.lex.populate(path);
            System.out.println(GlobalVarsStore.printTime(new Date())+": "+lexicon+" parsed for "+GlobalVarsStore.lex.getWordCount()+" words. ");
        }
    }
}
