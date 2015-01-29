/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

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
    public static Double[] threshold=new Double[]{0.005,0.008};
    
    public static String printTime(Date date) {
        return date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    }
}
