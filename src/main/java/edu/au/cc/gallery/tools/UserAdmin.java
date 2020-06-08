package edu.au.cc.gallery;

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
				System.out.println("2");
				break;
			case 3:
				System.out.println("3");
				break;
			case 4:
				System.out.println("4");
				break;
			case 5:
				System.out.println("Quitting");
				break;
			default:
				System.out.println("Not a valid option");
				break;
			}
		} while (opt !=5);
	}	





}
