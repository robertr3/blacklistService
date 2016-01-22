package com.silentDynamics.jersey;
import java.sql.SQLException;
import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
//Path: http://localhost/<appln-folder-name>/registerevent
@Path("/registerevent")
public class RegisterEvent {
    // HTTP Get Method
    @GET
    // Path: http://localhost/<appln-folder-name>/registerevent/doregisterevent
    @Path("/doregisterevent")  
    // Produces JSON as response
    @Produces(MediaType.APPLICATION_JSON) 
    // Query parameters are parameters: http://localhost/<appln-folder-name>/registerevent/doregisterevent?name=pqrs&username=abc&password=xyz
    public String doLogin(@QueryParam("name") String name, @QueryParam("type") String type,
    		@QueryParam("topic1") String topic1, @QueryParam("timeStart") Date timeStart, @QueryParam("timeEnd") Date timeEnd,
    		@QueryParam("location") String location, @QueryParam("privacy") Boolean privacy, @QueryParam("username") String username){
        String response = "";
        //System.out.println("Inside doLogin "+uname+"  "+pwd);
        int retCode = registerEvent(name, type, topic1, timeStart, timeEnd,
        		location, privacy, username);
        if(retCode == 0){
            response = Utility.constructJSON("registerEvent",true);
        }else if(retCode == 1){
            response = Utility.constructJSON("registerEvent",false, "You are already registered");
        }else if(retCode == 2){
            response = Utility.constructJSON("registerEvent",false, "Special Characters are not allowed in Username and Password");
        }else if(retCode == 3){
            response = Utility.constructJSON("registerEvent",false, "Error occured");
        }
        return response;
 
    }
 
    private int registerEvent(String name, String type, String topic1, Date timeStart, Date timeEnd,
    		String location, Boolean privacy, String username){
        System.out.println("Inside registerEvent");
        int result = 3;
        if(Utility.isNotNull(name) && Utility.isNotNull(type) && Utility.isNotNull(topic1) && Utility.isNotNull(timeStart) && Utility.isNotNull(location) && Utility.isNotNull(username)){
            try {
                if(DBConnection.insertEvent(name, type, topic1, timeStart, timeEnd,
                		location, privacy, username)){
                    System.out.println("RegisterUSer if");
                    result = 0;
                }
            } catch(SQLException sqle){
                System.out.println("RegisterUSer catch sqle");
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
        }else{
            System.out.println("Inside registerEvent else");
            result = 3;
        }
 
        return result;
    }
 
}