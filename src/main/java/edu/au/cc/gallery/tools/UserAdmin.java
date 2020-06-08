package edu.au.cc.gallery.tools;
import edu.au.cc.gallery.DB;
import java.util.Scanner;



public class UserAdmin{
	public void mainMenu() throws Exception {
		System.out.println("Welcome\n");
		int opt;
		do {
			System.out.println("Select an option \n" + "1) List users \n" +  "2) Add user \n" + "3) Edit user \n"  + "4) Delete user \n" + "5) Quit");
			Scanner sc = new Scanner(System.in);
			opt=Integer.parseInt(sc.next());
			switch (opt) {
			case 1:
			
				DB.ListUsers();
				
				break;
			case 2:
				String username,password,full_name;
                		 System.out.println("Username:");
                  		Scanner fc = new Scanner(System.in);
                 		username=fc.nextLine();
                 		System.out.println("Password:");
                		 password=fc.nextLine();
                 		System.out.println("Full Name:");
                 		full_name=fc.nextLine();
                 		DB.addUserChecks(username.toLowerCase(),password.toLowerCase(),full_name.toLowerCase());
				break;
			case 3:
					String usernameToEdit,newPass = null,newFull=null;
				System.out.println("Enter username to edit");
				Scanner fc2 = new Scanner(System.in);
                		usernameToEdit=fc2.nextLine();               
                		if(!DB.EditUserExists(usernameToEdit.toLowerCase())) {
                			break;
               			 }
                
             		  	 System.out.println("New password (press enter to keep current)");
         		       newPass=fc2.nextLine();
        		        System.out.println("New full name (press enter to keep current)");
      			          newFull=fc2.nextLine();
		                DB.EditUserChecks(usernameToEdit.toLowerCase(), newPass.toLowerCase(), newFull.toLowerCase());break;
			case 4:
				String usernameToDelete,confirmation;
				System.out.println("Enter username to delete");
				Scanner fc3 = new Scanner(System.in);
        		        usernameToDelete=fc3.nextLine();
               			 System.out.println("Are you sure that you want to delete " + usernameToDelete +" ?");
				confirmation=fc3.nextLine();
				if(confirmation.toLowerCase().equals("yes")) {
					DB.deleteUserChecks(usernameToDelete.toLowerCase());
				}
				break;
			case 5:
				System.out.println("Bye.");
				break;
			default:
				System.out.println("Not a valid option");
				break;
			}
		} while (opt !=5);
	}	





}
