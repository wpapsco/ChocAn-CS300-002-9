/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chocan;

import java.util.Scanner;

/**
 *
 * @author ashle
 */
public class ChocAn {
    
    private String output;

    public static void main(String[] args)
    {
        //initialize variables
        
        //call user input to prompt user/providerid
            
        //if validated, continue...
        
        //call menu for provider OR call menu for manager
        
        Scanner s= new Scanner(System.in);
        int key_temp=0;
        
        key_temp=new ChocAn().find_key();
        
        //idk if this is how we'll tell initally if they're managers/providers??
       
         System.out.println("Are you a (1) provider or a (2) manager?");
         key_temp=s.nextInt();
        
         if(key_temp ==1)
             new ChocAn().provider_menu(key_temp);
         
         if(key_temp == 2)
             new ChocAn().manager_menu(key_temp);
                 
    }
    
    int find_key()
    {
        int key_temp=0;
        int found=0;
        Scanner s= new Scanner(System.in);
        
        System.out.println("Please enter your id key  :     ");
        key_temp=s.nextInt();
        
        //call function to search manager/provider id list to find match
            //-> return 1 if found
            //-> return 2 if not found
            
        //id was found
        if(found == 1)
        {
            System.out.println("-ACCESS GRANTED-");
            return 1;
        }
        
        //error - id was not found
        if(found == 2)
        {
            int temp_input;
            
            System.out.println("-INCORRECT ID-");
            System.out.println("Would you like to "
                    + "1) re-enter id"
                    + "2) exit program");
            temp_input=s.nextInt();
            
            if(temp_input==1)
                find_key();
            
                 
            else if (temp_input==2)
                return 0;
        }
        
        
        return 0;
    }

    int provider_menu(int key)
    {
        Scanner s= new Scanner(System.in);
        int temp_input=0;
        
        System.out.println("  PROVIDER  # " + key);  
        System.out.println("  -SELECT AN OPTION -  "
                + "1) Varify ChocAn member ID"
                + "2) Bill ChocAn member for service"
                + "3) Look up service code"
                + "4) request weekly service summary"
                + "5) exit program");
        temp_input=s.nextInt();
        
        //call function to return status of member id
        if(temp_input ==1)
        {
            //call function....
            
            if(temp_input == 1)
                System.out.println(" MEMBER # " + key + "  :  VALIDATED");  
            
            if(temp_input ==2)
                System.out.println(" MEMBER # " + key + "  :  SUSPENDED"); 
            
            if(temp_input ==0)
                System.out.println(" MEMBER # " + key + "  :  SUSPENDED"); 
            
            return provider_menu(key);
        }
        
        //Call function to enter info to bill member
            //current time/ date, date of service, member if
            // member info (name, address etc.), service code (+ info), price
            
        else if(temp_input == 2)
        {
            //call function
            
            return provider_menu(key);
        }
        
        //call function to look up service code/display
        else if(temp_input == 3)
        {
            return provider_menu(key);
        }
        
        //request weekly service summary
        else if(temp_input == 4)
        {
            return provider_menu(key);
        }
        
        //exiting program
        else if(temp_input == 5)
        {
            System.out.println(" MEMBER # " + key + "  LOGGING OUT"); 
            return 0;
        }
        
        return 0;
    }
    
     int manager_menu(int key)
    {
         Scanner s= new Scanner(System.in);
        int temp_input=0;
        
        System.out.println("  MANAGER  # " + key);  
        System.out.println("  -SELECT AN OPTION -  "
                + "1) add / remove member "
                + "2) modify member info"
                + "3) view ChocAn member list"
                + "4) view Provider list"
                + "5) view weekly summaries"
                + "6) exit program");
        temp_input=s.nextInt();
        
        //add / remove a member
        if(temp_input == 1)
        {
            //call function
            return manager_menu(key);
        }
        
        //function to modify info 
        else if(temp_input == 2)
        {
            //call function...
             return manager_menu(key);
        }
        
        //function to view member list
        else if(temp_input ==3)
        {
            //call function 
            return manager_menu(key);

        }
        
        //function to view provider list
        else if(temp_input == 4)
        {
            //call function 
            return manager_menu(key);

        }
        
         //function to display any of three summary reports
        else if(temp_input == 5)
        {
            //call function 
            return manager_menu(key);

        }
        
        //exiting program
        else if(temp_input == 5)
        {
            System.out.println( " LOGGING OUT "); 
            return 0;
        }
        
        return 0;
    }
//////////////////////////////////////////////////////////////
     
     

    
}

