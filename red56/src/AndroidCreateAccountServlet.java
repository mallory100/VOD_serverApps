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


public class AndroidCreateAccountServlet extends HttpServlet {
	
	protected String generateKey(String user){
		Key key = MacProvider.generateKey();

		String compactJws = Jwts.builder()
				.setSubject(user)
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
		
		assert Jwts.parser().setSigningKey(key).parseClaimsJws(compactJws).getBody().getSubject().equals(user);
		return compactJws;
			
	}
		
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
       
        boolean doesUserExistInDB = true ;  
        String token;
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
 
        Connection con = DBConnectionHandler.getConnection();
        String sqlSelectUserName = " SELECT login, pass FROM users where login=?";
        
        try {
            PreparedStatement ps = con.prepareStatement(sqlSelectUserName);
            ps.setString(1, params[0]);
            ResultSet rs = ps.executeQuery();

        // Executing SQL (select user to check if exist in DB) on connected SQL data base
            if (rs.next()) { 
            	doesUserExistInDB = true;
                json.put("info", "fail");
               
            } else {
            	doesUserExistInDB = false;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String sqlInsertUserData = "INSERT INTO users (login, pass, token) values (?,?,?)"; // 
  //      String sqlSelectUserData = "SELECT login, pass FROM users where login=? and pass=?";
    //    String sqlInsert = "UPDATE users SET token=? where login=? and pass=?";
        
        // Executing SQL (inserting user data) on connected SQL data base
        if (doesUserExistInDB == false){
        try {
            token = generateKey(params[0]);
            System.out.println(token);
            PreparedStatement ps = con.prepareStatement(sqlInsertUserData);
            ps.setString(1, params[0]);
            ps.setString(2, params[1]);
            ps.setString(3, token);
            ps.executeUpdate(); //added token for new user 
            json.put("info", "success");
          //  json.put("token", token);
                      
         } catch (Exception e) {
            e.printStackTrace();
            json.put("info", "fail");
        }
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