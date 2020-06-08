package edu.au.cc.gallery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB{
        private static final String dbUrl="jdbc:postgresql://database-1.c5aarzjcoddf.us-east-2.rds.amazonaws.com/image_gallery";
        private Connection connection;
        private String getPassword(){
          try(BufferedReader br = new BufferedReader(new FileReader("/home/ec2-user/.sql-passwd"))){
             String result = br.readLine();
              return result;
          } catch (IOException ex){
                System.err.println("Error opening password file");
                System.exit(1);
          }
                return null;
        }

     public void connect() throws SQLException{
                try{
                  Class.forName("org.postgresql.Driver");
                  connection = DriverManager.getConnection(dbUrl, "image_gallery", getPassword());
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
                        System.out.println(rs.getString(1)+"   		  " + rs.getString(2) + "	     " + rs.getString(3)+"\n");
		}
                rs.close();
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

	//public static void










}

