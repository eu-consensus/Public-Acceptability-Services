/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author ViP
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(RestServices.Aggregate.class);
        resources.add(RestServices.GetDaily.class);
        resources.add(RestServices.Scoresfile.class);
        resources.add(RestServices.Scoresservice.class);
        resources.add(RestServices.Scoresserviceall.class);
        resources.add(RestServices.Scoresxml.class);
    }
    
}
