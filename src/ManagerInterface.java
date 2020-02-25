import java.util.Scanner;

public class ManagerInterface {
    public static DatabaseInterface database;

    ManagerInterface(DatabaseInterface database){this.database = database;}

    //clears the standard output device
    //code from Abhishek Kashyap on stackoverflow.com
    public final static void clearConsole(){
        try{
            if(System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                System.out.print("\033\143");
        } catch(final Exception e){}
    }

    //Menu interface
    public static void main(String[] args){
        Scanner userIn = new Scanner(System.in);
        int selection = 0;

        while(true){
            clearConsole();
            //validate user selection
            boolean valid = false;
            do{
                printMenu();
                while(!userIn.hasNextInt()){
                    clearConsole();
                    System.out.println("Invalid selection.\n");
                    userIn.next();
                    printMenu();
                }
                selection = userIn.nextInt();
                if(selection >= 1 && selection <= 5)
                    valid = true;
                else{
                    clearConsole();
                    System.out.println("Invalid selection.\n");
                }
            } while(!valid);

            //process user selection
            switch (selection) {
                //still need to catch and process return values
                case 1: providerReport();
                    valid = true;
                    break;
                case 2: memberReport();
                    valid = true;
                    break;
                case 3: editProvider();
                    valid = true;
                    break;
                case 4: editMember();
                    valid = true;
                    break;
                case 5: return;
                //should never reach default, if we do, something went wrong
                default: assert false;
            }
        }
    }

    //print manager options menu to System.out
    private static void printMenu(){
        System.out.println(
                "1. Compile a service report for each provider.\n" +
                        "2. Compile a service report for each member.\n" +
                        "3. Add, remove, or update provider info.\n" +
                        "4. Add, remove, or update member info.\n" +
                        "5. Logout.\n" +
                        "Make a selection: ");
    }
    // Create a provider report and save to an external data file
    private static int providerReport(){
        System.out.println("Provider report not implemented yet!");
        return 0;
    }

    // Create a member report and save to an external data file
    private static int memberReport(){
        System.out.println("Member report not implemented yet!");
        return 0;
    }

    // Add, remove, or update member information
    private static int editMember(){
        System.out.println("Edit member not implemented yet!");
        return 0;
    }

    // Add, remove, or update provider records
    private static int editProvider(){
        System.out.println("Edit provider not implemented yet!");
        return 0;
    }
}
