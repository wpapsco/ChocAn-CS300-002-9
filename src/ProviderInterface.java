import java.util.Scanner;
// Assumptions: - 0 is an invalid member ID
//              - member IDs are always positive integers

public class ProviderInterface {
    public static DatabaseInterface database;
    private static int ID;
    ProviderInterface(DatabaseInterface database, int ID){this.database = database; this.ID = ID;}

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
                case 1: checkID();
                    valid = true;
                    break;
                case 2: chargeMember();
                    valid = true;
                    break;
                case 3: checkProviderDirectory();
                    valid = true;
                    break;
                case 4: serviceReport();
                    valid = true;
                    break;
                case 5: return;
                //should never reach default, if we do, something went wrong
                default: assert false;
            }
        }
    }

    //print provider options menu to System.out
    private static void printMenu(){
        System.out.println(
                "1. Check member status.\n" +
                        "2. Log a service.\n" +
                        "3. Lookup service by name.\n" +
                        "4. Generate service report(last 7 days).\n" +
                        "5. Logout.\n" +
                        "Make a selection: ");
    }

    // Check member ID number for status (valid, suspended, or invalid)
    private static void checkID(){
        Scanner sc = new Scanner(System.in);
        int memberID = 0;

        // Loop until user enters reasonable member ID
        do {
            System.out.print("Enter member ID: ");
            String input = sc.nextLine();

            // member ID must be a positive int
            try {
                memberID = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid Number.");
                memberID = 0;
            }
            if(memberID < 0) {
                System.out.println("Invalid Number.");
                memberID = 0;
            }
        } while(memberID == 0);

        // Lookup member in database, return status or no match
        int databaseRetValue = callDatabaseFunction(memberID);
        switch(databaseRetValue) {
            case 1:
                System.out.println("Invalid Number");
                break;
            case 2:
                System.out.println("Validated");
                break;
            case 3:
                System.out.println("Member suspended");
                break;
        }
    }

    // Charge a member for a service
    private static int chargeMember(){
        return 0;
    }

    //log a service to the database
    private static int logService(){
        ServiceRecord log = new ServiceRecord();
        Scanner in = new Scanner(System.in);

        return 0;
    }

    // Lookup a service and print the service name, code, and fee if valid
    private static int checkProviderDirectory(){
        return 0;
    }

    //generates service report for the past 7 days
    //returns true on success
    private static boolean serviceReport(){
        return true;
    }
}
