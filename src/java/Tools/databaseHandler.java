/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ViP
 */
public class databaseHandler {
    // JDBC driver name and database URL
   //String JDBC_DRIVER;  
   //String DB_URL;

   //  Database credentials
   //String USER;
   //String PASS;
    
    private static DataSource datasource;

    public databaseHandler() {
        //if(JDBC_DRIVER==null){
        //    this.JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
        //}else{
        //    this.JDBC_DRIVER = JDBC_DRIVER;
        //}
        //this.DB_URL = DB_URL;
        //this.USER = USER;
        //this.PASS = PASS;
        if(datasource==null){
            try{
                datasource = (DataSource) new InitialContext().lookup(GlobalVarsStore.databasePool);
            }
            catch (NamingException e){
                    throw new ExceptionInInitializerError(e);
            }
        }
    }
    
   public boolean checkTables(){
        Connection conn = null;
        Statement stmt = null;
        int inserted=0;
        try{
           //Class.forName(this.JDBC_DRIVER);
           //conn = DriverManager.getConnection(DB_URL,USER,PASS);
           conn = datasource.getConnection();
           stmt = conn.createStatement();
           String sql = "CREATE TABLE IF NOT EXISTS `PublicAcceptabilityScores` (`Keyword` VARCHAR(120) NOT NULL,`SOS` DOUBLE NOT NULL,`SOF` DOUBLE NOT NULL, PRIMARY KEY (`Keyword`));";
           inserted = stmt.executeUpdate(sql);
           sql = "CREATE TABLE IF NOT EXISTS `PublicAcceptabilityScoresDaily` (`Keyword` VARCHAR(120) NOT NULL,`SOS` DOUBLE NOT NULL,`SOF` DOUBLE NOT NULL, `Date` DATE NOT NULL ,PRIMARY KEY (`Keyword`,`Date`));";
           stmt.executeUpdate(sql);
           stmt.close();
           conn.close();
        }catch(SQLException se){se.printStackTrace(); return false;
        }catch(Exception e){e.printStackTrace(); return false;
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
                 stmt.close();
           }catch(SQLException se2){
           }// nothing we can do
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        if(inserted>0) return true;
        else return false;
   }
   
   public boolean insert(String keyword, Double sos, Double sof) {
   Connection conn = null;
   Statement stmt = null;
   int inserted=0;
   try{
      //Class.forName(this.JDBC_DRIVER);
      //conn = DriverManager.getConnection(DB_URL,USER,PASS);
      conn = datasource.getConnection();
      stmt = conn.createStatement();
      String sql;
      sql = "INSERT INTO PublicAcceptabilityScores ( Keyword, SOF, SOS ) VALUES ( '"+keyword+"',"+sof+","+sos+" ) ON DUPLICATE KEY UPDATE SOF=VALUES(SOF), SOS=VALUES(SOS);";
      System.out.println(sql);
      inserted = stmt.executeUpdate(sql);
      sql = "INSERT INTO PublicAcceptabilityScoresDaily ( Keyword, SOF, SOS , Date ) VALUES ( '"+keyword+"',"+sof+","+sos+",CURDATE() ) ON DUPLICATE KEY UPDATE SOF=VALUES(SOF), SOS=VALUES(SOS);";
      inserted = stmt.executeUpdate(sql);
      stmt.close();
      conn.close();
   }catch(SQLException se){se.printStackTrace(); return false;
   }catch(Exception e){e.printStackTrace(); return false;
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   if(inserted>0) return true;
   else return false;
}
   public ArrayList<Double[]> readObjectiveScores(String scenario, Integer objective){
       ArrayList<Double[]> res=new ArrayList<Double[]>();
       CrawlersConnector ccn=new CrawlersConnector();
       ArrayList<String> keys=null;
        if(scenario.equalsIgnoreCase("transportation")){
            if(objective==1){keys=ccn.transportKeywords1;}
            else if(objective==2){keys=ccn.transportKeywords2;}
            else if(objective==3){keys=ccn.transportKeywords3;}
            else if(objective==4){keys=ccn.transportKeywords4;}
            else if(objective==5){keys=ccn.transportKeywords5;}
            else if(objective==6){keys=ccn.transportKeywords6;}
        }
        String keyChain="WHERE Keyword = '";
        int i;
        for (i = 0; i < keys.size()-1; i++) {
           keyChain+=keys.get(i)+"' OR Keyword = '";
        }
        keyChain+=keys.get(i)+"';";
       
        Connection conn = null;
        Statement stmt = null;
        try{
           //Class.forName(this.JDBC_DRIVER);
           //conn = DriverManager.getConnection(DB_URL,USER,PASS);
           conn = datasource.getConnection();
           stmt = conn.createStatement();
           String sql = "SELECT SOS,SOF FROM PublicAcceptabilityScores "+keyChain;
           ResultSet rs = stmt.executeQuery(sql);
           while(rs.next()){
               Double[] cur=new Double[2];
               cur[0]=rs.getDouble("SOS");
               cur[1]=rs.getDouble("SOF");
               res.add(cur);
           }
           stmt.close();
           conn.close();
        }catch(SQLException se){se.printStackTrace(); return null;
        }catch(Exception e){e.printStackTrace();  return null;
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
                 stmt.close();
           }catch(SQLException se2){
           }// nothing we can do
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        
       return res;
   }
   
   public String readDailyLogs(String scenario, Integer objective){
       String res="";
       CrawlersConnector ccn=new CrawlersConnector();
       ArrayList<String> keys=null;
        if(scenario.equalsIgnoreCase("transportation")){
            if(objective==1){keys=ccn.transportKeywords1;}
            else if(objective==2){keys=ccn.transportKeywords2;}
            else if(objective==3){keys=ccn.transportKeywords3;}
            else if(objective==4){keys=ccn.transportKeywords4;}
            else if(objective==5){keys=ccn.transportKeywords5;}
            else if(objective==6){keys=ccn.transportKeywords6;}
        }
        String keyChain="WHERE Keyword = '";
        int i;
        for (i = 0; i < keys.size()-1; i++) {
           keyChain+=keys.get(i)+"' OR Keyword = '";
        }
        keyChain+=keys.get(i)+"';";
       
        Connection conn = null;
        Statement stmt = null;
        try{
           //Class.forName(this.JDBC_DRIVER);
           //conn = DriverManager.getConnection(DB_URL,USER,PASS);
           conn = datasource.getConnection();
           stmt = conn.createStatement();
           String sql = "SELECT * FROM PublicAcceptabilityScoresDaily "+keyChain;
           ResultSet rs = stmt.executeQuery(sql);
           try{
            res=convertDailyLogs(rs).toString();
           }catch(Exception ex){res=ex.getMessage();}
           stmt.close();
           conn.close();
        }catch(SQLException se){se.printStackTrace(); return null;
        }catch(Exception e){e.printStackTrace();  return null;
        }finally{
           //finally block used to close resources
           try{
              if(stmt!=null)
                 stmt.close();
           }catch(SQLException se2){
           }// nothing we can do
           try{
              if(conn!=null)
                 conn.close();
           }catch(SQLException se){
              se.printStackTrace();
           }//end finally try
        }//end try
        
       return res;
   }
   
   private static JSONArray convertDailyLogs( ResultSet rs )
    throws SQLException, JSONException
  {
    JSONArray json = new JSONArray();
    ArrayList<String> dates=new ArrayList<String>();
    ArrayList<JSONArray> inners=new ArrayList<JSONArray>();
    
    while(rs.next()) {
      JSONObject objInner = new JSONObject();
      objInner.put("SOS", rs.getDouble(2)); 
      objInner.put("SOF", rs.getDouble(3)); 
      objInner.put("Keyword", rs.getString(1));
      String date=rs.getDate(4).toString();
      int index=dates.indexOf(date);
      if(index<0){
          dates.add(date);
          JSONArray jsonInner = new JSONArray();
          jsonInner.put(objInner);
          inners.add(jsonInner);
      }else{
          inners.get(index).put(objInner);
      }
    }
    
      for (int i = 0; i < dates.size(); i++) {
          JSONObject outer=new JSONObject();
          outer.put(dates.get(i),inners.get(i));
          json.put(outer);
      }
    
    return json;
  }
   
   private static JSONArray convert( ResultSet rs )
    throws SQLException, JSONException
  {
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = rs.getMetaData();
    int numColumns = rsmd.getColumnCount();
    
    while(rs.next()) {
      JSONObject obj = new JSONObject();

      for (int i=1; i<numColumns+1; i++) {
        String column_name = rsmd.getColumnName(i);

        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
         obj.put(column_name, rs.getArray(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
         obj.put(column_name, rs.getBoolean(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
         obj.put(column_name, rs.getBlob(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
         obj.put(column_name, rs.getDouble(column_name)); 
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
         obj.put(column_name, rs.getFloat(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
         obj.put(column_name, rs.getNString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
         obj.put(column_name, rs.getString(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
         obj.put(column_name, rs.getInt(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
         obj.put(column_name, rs.getDate(column_name));
        }
        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
        obj.put(column_name, rs.getTimestamp(column_name));   
        }
        else{
         obj.put(column_name, rs.getObject(column_name));
        }
      }

      json.put(obj);
    }

    return json;
  }
}
