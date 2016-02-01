package com.silentDynamics.jersey;
import java.sql.SQLException;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
//Path: http://localhost/<appln-folder-name>/getevents
@Path("/getevents")
public class GetEvents {
    // HTTP Get Method
    @GET
    // Path: http://localhost/<appln-folder-name>/getevents/dogetevents
    @Path("/dogetevents")  
    // Produces JSON as response
    @Produces(MediaType.APPLICATION_JSON) 
    // Query parameters are parameters: http://localhost/<appln-folder-name>/getevents/dogetevents?username=pqrs&timestart=fgh&topic1=xyz&location=abc
    public String doLogin(@QueryParam("username") String username,
    		@QueryParam("topic1") String topic1, @QueryParam("timestart") String timestart,
    		@QueryParam("location") String location){
        String response = "";
        int retCode = 3;
        System.out.println("Inside doLogin "+username +" "+ timestart );
        if (Utility.isNotNull(username) && !Utility.isNotNull(topic1) && !Utility.isNotNull(location) && !Utility.isNotNull(timestart)){
        	response = getEvent("username", username);
        }
        else if (!Utility.isNotNull(username) && Utility.isNotNull(topic1) && !Utility.isNotNull(location) && !Utility.isNotNull(timestart)){
        	response = getEvent("topic", topic1);
        }
        else if (!Utility.isNotNull(username) && !Utility.isNotNull(topic1) && Utility.isNotNull(location) && !Utility.isNotNull(timestart)){
        	response = getEvent("location", location);
        }
        else if (!Utility.isNotNull(username) && !Utility.isNotNull(topic1) && !Utility.isNotNull(location) && Utility.isNotNull(timestart)){
        	response = getEvent("timestart", timestart);
        }
        else{
            System.out.println("Inside registerEvent else");
            retCode = 3;
        }
     /*   if(retCode == 0){
            response = Utility.constructJSON("getEvent",true);
        }else if(retCode == 1){
            response = Utility.constructJSON("getEvent",false, "You are already registered");
        }else if(retCode == 2){
            response = Utility.constructJSON("getEvent",false, "Special Characters are not allowed in Username and Password");
        }else if(retCode == 3){
            response = Utility.constructJSON("getEvent",false, "Error occured");
        }*/
        return response;
 
    }
 
    private String getEvent(String type, String value){
        System.out.println("Inside registerEvent" + value);
        int result = 3;
        String response = "";
            try {
                response = DBConnection.getEvent(type, value);
                    System.out.println("get Events by username");
                    result = 0;
            } catch(SQLException sqle){
                System.out.println("RegisterUSer catch sqle" + sqle);
                //When Primary key violation occurs that means user is already registered
                if(sqle.getErrorCode() == 1062){
                    result = 1;
                } 
                //When special characters are used in name,username or password
                else if(sqle.getErrorCode() == 1064){
                    System.out.println(sqle.getErrorCode());
                    result = 2;
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Inside checkCredentials catch e ");
                result = 3;
            }
 
        return response;
    }
 
}