/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import Tools.databaseHandler;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author ViP
 */
@Path("getDaily")
public class GetDaily {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetDaily
     */
    public GetDaily() {
    }

    /**
     * Retrieves representation of an instance of RestServices.GetDaily
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@QueryParam("scenario") String scenario, @QueryParam("objective") Integer objective) {
        databaseHandler dbh=new databaseHandler();
        dbh.checkTables();
        return dbh.readDailyLogs(scenario, objective);
    }

    /**
     * PUT method for updating or creating an instance of GetDaily
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
