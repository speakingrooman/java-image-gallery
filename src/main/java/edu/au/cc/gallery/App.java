/*
 * This Java source file was generated by the Gradle 'init' task.
 */


package edu.au.cc.gallery;
import edu.au.cc.gallery.tools.UserAdmin;
import edu.au.cc.gallery.DB;
import edu.au.cc.gallery.tools.secrets;
import static spark.Spark.*;
import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.List;
import spark.Request;
import spark.Response;

public class App {
    public String getGreeting() {
        return "Hello Mohammad.";
    }





    public static void main(String[] args) throws Exception {
 //System.out.println(new App().getGreeting());

 //port(5000);
  //S3.demo();
//  DB.demo();
// UserAdmin ua = new UserAdmin();
   // ua.mainMenu();

  String portString = System.getenv("JETTY_PORT");
	if (portString == null || portString.equals(""))
	    port(5000);
	else
	    port(Integer.parseInt(portString));
	get("/hello",(req,res) -> "<!DOCTYPE html><html><head><title>Hello</title><meta charset='utf-8' /></head><body><h1>Hello World</h1></body></html>");


	post("/add",(req,res) -> "The sum is " + (Integer.parseInt(req.queryParams("x"))+ Integer.parseInt(req.queryParams("y"))));

	get("/calculator",(req,res) -> {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("name","Fred");
		return new HandlebarsTemplateEngine().render(new ModelAndView(model,"calculator.hbs"));
	});
	
	get("/admin",(req,res) -> {
                Map<String,Object> model = new HashMap<String,Object>();
                model.put("User",DB.executeUI());
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"admin.hbs"));
        });

	//get("/admin/:name", (req,res) ->"Hello " +  req.params(":name"));

        get("/admin/:name",(req,res) -> {
                Map<String,Object> model = new HashMap<String,Object>();
                model.put("User",req.params(":name"));
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"edituser.hbs"));
        });


	  post("/admin/edit",(req,res) ->{
                        DB.EditUserChecks(req.queryParams("user"), req.queryParams("pass"),req.queryParams("full"));

                        Map<String,Object> model = new HashMap<String,Object>();
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"edituser.hbs"));
      });



      get("/admin/delete/:name",(req,res) -> {
                Map<String,Object> model = new HashMap<String,Object>();
                model.put("User",req.params(":name"));
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"deleteuser.hbs"));
        });


          post("/admin/delete/:name",(req,res) ->{
                        DB.deleteUserChecks(req.params(":name"));

                        Map<String,Object> model = new HashMap<String,Object>();
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"deleteuser.hbs"));
      });







      
	
	
	
	
	post("/admin/create",(req,res) ->{ 
			DB.addUserChecks(req.queryParams("user"), req.queryParams("pass"),req.queryParams("full"));

			Map<String,Object> model = new HashMap<String,Object>();
                model.put("name",req.queryParams("full"));
		model.put("username",req.queryParams("user"));
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"newuser.hbs"));
      });


       get("/admin/create",(req,res) ->{
                        DB.addUserChecks(req.queryParams("user"), req.queryParams("pass"),req.queryParams("full"));

                        Map<String,Object> model = new HashMap<String,Object>();
                model.put("name",req.queryParams("full"));
                model.put("username",req.queryParams("user"));
                return new HandlebarsTemplateEngine().render(new ModelAndView(model,"newuser.hbs"));
      });


 

	
       get("/sessionDemo", (req,res) -> sessionDemo(req,res));
	  get("/login", (req,res) ->login(req,res));
       post("/login", (req,res) ->loginPost(req,res));

	before("/admin/*", (request,response) -> checkAdmin(request,response));
       get("/debugSession", (req,res) -> debugSession(req,res));
      	 before("/"+"^(?!login).*$", (request,response) -> checkUserAuth(request,response)); 
			       



  
  }

private static boolean isAdmin(String username){

	return username!= null && username.equals("admin");
}


private static void checkAdmin(Request req, Response resp){
	if(!isAdmin(req.session().attribute("user"))){
		resp.redirect("/login");
		halt();
	}

}

private static void checkUserAuth(Request req, Response resp){
	 if(req.session().attribute("user")==null || req.session().attribute("password")==null){
                resp.redirect("/login");
		halt();
               
        }

}






private static String login(Request req, Response resp){
	Map<String,Object> model = new HashMap<String,Object>();
	return new HandlebarsTemplateEngine().render(new ModelAndView(model,"login.hbs"));


}

private static String loginPost(Request req, Response resp){

	try{
		String username=req.queryParams("username");
		String password = req.queryParams("password");
		if(!DB.UserCheckLogin(username) || username==null || !DB.UserCheckLoginPasswordMatch(username,password)){
			return "Invalid user or password";
		}
		req.session().attribute("user",username);
		req.session().attribute("password",password);
		resp.redirect("/");
	} catch (Exception ex){
		return "Error" + ex.getMessage();
	}
	return "";
}



 private static String sessionDemo(Request req, Response resp){
        if(req.session().isNew()){
                req.session().attribute("owner","fred");
                 req.session().attribute("foo","bar");
        } else {

        }
         return "stored";

       }


       private static String debugSession(Request req, Response resp){
               StringBuffer sb = new StringBuffer();
        for(String key:req.session().attributes()){
                sb.append(key+"->"+req.session().attribute(key)+"<br/>");
        }
        return sb.toString();
      }
}
