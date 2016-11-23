import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.JsonPrimitive;



public class AndroidVideosDataServlet extends HttpServlet{
	
	
	//Hangle HTTP GET request for movies data 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
      response.setContentType("text/html;charset=UTF-8");
      JSONObject jsonResponse = new JSONObject();  
      JSONArray data = new JSONArray();
      JSONArray data1= new JSONArray();
            
        try {
    
           // PrintWriter out = response.getWriter();
            Connection con = DBConnectionHandler.getConnection();
            String sql = "SELECT videoname, videourl, videodescription FROM videos";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
            {
            	
            	JSONObject json_video = new JSONObject();
            	
            	json_video.put("videoname", new JsonPrimitive(rs.getString("videoname")));
            	json_video.put("videourl", new JsonPrimitive(rs.getString("videourl")));
            	json_video.put("videodescription", new JsonPrimitive(rs.getString("videodescription")));
            	
            	data.add(json_video);
            	
            	
            	/*
            	
            	JsonObject jo = Json.createObjectBuilder()
            			  .add("employees", Json.createArrayBuilder()
            			    .add(Json.createObjectBuilder()
            			      .add("firstName", "John")
            			      .add("lastName", "Doe")))
            			  .build();*/

            }

            
            
            jsonResponse.put("Video", data);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
            
            System.out.println(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        

    }
	
	
	


