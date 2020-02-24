import java.util.Objects;
import java.util.Scanner;

public class Login {

    public static void main(String[] args){

        System.out.println("Welcome to ChocAn!");

        loginMainMenu();
    }

    public static void loginMainMenu(){
        Scanner loginInput = new Scanner(System.in);

        int loginKey;

        do {
            System.out.println("Press 1 for provider login.\n" +
                    "Press 2 for manager login.");
            while(!loginInput.hasNextInt()){
                System.out.println("Invalid selection.");
                loginInput.next();
            }
            loginKey = loginInput.nextInt();
            if(loginKey != 1 && loginKey != 2){
                System.out.println("Invalid selection.");
            }
        } while (loginKey != 1 && loginKey != 2);

        if(loginKey == 1){
            ProviderLogin();
        }
        else{
            ManagerLogin();
        }
    }

    private static void ProviderLogin(){
        Scanner loginID = new Scanner(System.in);
        int ID = 0;

        boolean found = false;

        do{
            System.out.println("Enter your provider ID or press x to return to main menu.");

            if(!loginID.hasNextInt() && loginID.hasNext("x")){
                loginMainMenu();
                break;
            }

            while(!loginID.hasNextInt()){
                System.out.println("Invalid ID. Please try again or press x to return to the main menu.");
                if (loginID.hasNext("x")) {
                    loginMainMenu();
                    break;
                }
                loginID.next();
            }



            ID = loginID.nextInt();

            //check id here


        } while (!found);

        //open provider interface here

    }

    public static void ManagerLogin(){

    }
}