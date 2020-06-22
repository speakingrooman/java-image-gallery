package edu.au.cc.gallery;

import edu.au.cc.gallery.tools.secrets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class DB{
        private static final String dbUrl="jdbc:postgresql://image-gallery.c5aarzjcoddf.us-east-2.rds.amazonaws.com/image_gallery";
        private static Connection connection;
	private JSONObject getSecret(){
		String s = secrets.getSecretImageGallery();
		return new JSONObject(s);

	}
        private String getPassword(JSONObject secret){
          //try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))){
             //String result = br.readLine();
            //  return result;
          //} catch (IOException ex){
              //  System.err.println("Error opening password file");
            //    System.exit(1);
          //}
        //        return null;
	       
	       
	       return secret.getString("password");
        }

     public void connect() throws SQLException{
                try{
                  Class.forName("org.postgresql.Driver");

		  JSONObject secret = getSecret();
         //         connection = DriverManager.getConnection(dbUrl, "image_gallery", "simple");
		  connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword(secret));
                } catch (ClassNotFoundException ex){
                        ex.printStackTrace();
                        System.exit(1);
                }
        }


        public void execute() throws SQLException{
                PreparedStatement ps = connection.prepareStatement("select * from users");
                ResultSet rs =  ps.executeQuery();
                while(rs.next()){
			System.out.println("Username"+"     Password"+"     Full Name");
                        System.out.println(rs.getString(1)+"   	   " + rs.getString(2) + "	 " + rs.getString(3)+"\n");
		}
                rs.close();
        }




	 public static Map<String,String> executeUI() throws SQLException{
		  DB db = new DB();
                db.connect();
		 Map<String,String> Users = new HashMap<>();
                PreparedStatement ps = connection.prepareStatement("select * from users");
                ResultSet rs =  ps.executeQuery();
                while(rs.next()){
         		Users.put(rs.getString(1),rs.getString(3));	
		}
                rs.close();
		db.close();
		return Users;
        }







        public void close() throws SQLException{
                connection.close();
        }


        public static void demo() throws Exception{
                DB db = new DB();
                db.connect();
               // db.execute();
                db.close();
        }


	public  static void ListUsers() throws Exception{
		 DB db = new DB();
                db.connect();
		db.execute();
		db.close();
	}

	public static void addUserChecks(String userName, String password, String full_name) throws Exception{
		 DB db = new DB();
		 db.connect();
        	 Boolean doesExist = db.DoesExist(userName.toLowerCase());

         	if(doesExist) {
        		 System.out.println("Error: user with username "+ userName + " already exists \n");
         	} else {
        	 	db.addToDB(userName.toLowerCase(),password.toLowerCase(),full_name.toLowerCase());
         	}
	}
	
	public void addToDB(String userName, String password, String full_name) throws Exception {
		 PreparedStatement ps = connection.prepareStatement("insert into users(username,password,full_name) values(?,?,?)");
	     	ps.setString(1, userName);
	    	 ps.setString(2, password);
	    	 ps.setString(3, full_name);
	   	  ps.executeUpdate();
	}
	
	
	
	public Boolean DoesExist(String userName) throws SQLException{
        	PreparedStatement ps = connection.prepareStatement("select * from users where username=?");
        	ps.setString(1, userName);
        	ResultSet rs =  ps.executeQuery();
        	while(!rs.next()){
//        		 rs.close();
        		return false;
        	}
//        	rs.close();
        	return true;
	}	
	
	
	public static void deleteUserChecks(String userName) throws Exception{
		 DB db = new DB();
		 db.connect();
       		 Boolean doesExist = db.DoesExist(userName.toLowerCase());

        	if(!doesExist) {
       	 		System.out.println("Error: user with username "+ userName + " does not exist \n");

        	} else {
       	
       	 		db.removeFromDB(userName.toLowerCase());
       		 	System.out.println("Deleted. \n");
        	}
	}
	
	public void removeFromDB(String userName) throws Exception {
		 PreparedStatement ps = connection.prepareStatement("delete from users where username=?");
		  ps.setString(1, userName);
	     	  ps.executeUpdate();
	}


	public static void EditUserChecks(String usernameToEdit, String newPass, String newFull) throws Exception{
		 DB db = new DB();
		 db.connect();

       	
        	  if(!newPass.equals("") && newFull.equals("")) {
              	db.editDBpassOnly(usernameToEdit,newPass);
              }
              if(!newFull.equals("") && newPass.equals("")) {
              	db.editDBfullOnly(usernameToEdit,newFull);
              }
              if(!newPass.equals("") && !newFull.equals("")) {
              	db.editDBpassfull(usernameToEdit,newPass,newFull);
              }
	}
	
	public void editDBpassOnly(String userName, String password) throws Exception{
		PreparedStatement ps = connection.prepareStatement("update users set password = ? where username=?");
	     ps.setString(1, password);
	     ps.setString(2, userName);
	     ps.executeUpdate();
	}

	public void editDBfullOnly(String userName,  String full_name) throws Exception{
		PreparedStatement ps = connection.prepareStatement("update users set full_name=? where username=?");
		    ps.setString(1, full_name);
		    ps.setString(2, userName);
		    ps.executeUpdate();
	}


	public void editDBpassfull(String userName, String password, String full_name) throws Exception{
		PreparedStatement ps = connection.prepareStatement("update users set password=?,full_name=? where username=?");
   		 ps.setString(1, password);
   	 	ps.setString(2, full_name);
   	 	ps.setString(3, userName);
    		ps.executeUpdate();
	}

	public static Boolean EditUserExists(String usernameToEdit) throws SQLException {
	 	DB db = new DB();
	 	db.connect();
    		Boolean doesExist = db.DoesExist(usernameToEdit.toLowerCase());
    		if(!doesExist) {
   	 		System.out.println("No such user. \n");
   		 	return false;
    		}
   		 return true;
	}




}

