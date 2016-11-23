import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Key;
import java.sql.*;
import java.util.Enumeration;

import org.json.simple.JSONObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;


public class AndroidLoginServlet extends HttpServlet {
	

   /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
	
	// Handle HTTP GET request
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        response.setContentType("text/html;charset=UTF-8");
        JSONObject json = new JSONObject();
       
         
        ResultSet token;
      //  ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
        Enumeration paramNames = request.getParameterNames();
        String params[] = new String[2];
        int i = 0;
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
 
            System.out.println(paramName);
            String[] paramValues = request.getParameterValues(paramName);
            params[i] = paramValues[0];
 
            System.out.println(params[i]);
            i++;
 
        }
 
        
        String sqlSelect = "SELECT token FROM users where login=? and token=?"; // 
        Connection con = DBConnectionHandler.getConnection();
        
        // Executing SQL statement on connected SQL data base
        try {
            PreparedStatement ps = con.prepareStatement(sqlSelect);
            
            ps.setString(1, params[0]);
            ps.setString(2, params[1]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                json.put("info", "success");
                json.put("token", rs.getObject("token"));
                System.out.println(json);                                  
                
                
            } else {
                json.put("info", "fail");
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
  
       
  

    }
 
    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}